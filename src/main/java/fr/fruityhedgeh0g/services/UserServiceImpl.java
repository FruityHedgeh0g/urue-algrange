package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.utilities.logging.Logged;

import fr.fruityhedgeh0g.dtos.userDtos.ProfileDto;
import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.enums.RoleEnum;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.ForbiddenRoleChangeException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.NotImplementedYetException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.keycloak.KeycloakRoleMirror;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalGroupService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalSectorService;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
@Logged
@ApplicationScoped
@Default
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    @Inject
    KeycloakRoleMirror keycloakRoleMirror;

    @Inject
    InternalGroupService internalGroupService;

    @Inject
    InternalSectorService internalSectorService;

    //Using UUID to test the existence of the user is acceptable because it is based on an external system (Keycloak)

    @Override
    public List<UserDto> listAll() {
        return userRepository.listAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto getById(UUID userId) {
        return userMapper.toDto(
                userRepository.findByIdOptional(userId)
                        .orElseThrow(() -> new UnknownResourceException("User not found: "+userId))
        );

    }

    @Override
    public UserDto changeRole(UUID actorId, UUID personId, RoleEnum role, UUID sectorId) {
        // Committed on its own before the Keycloak call, so a mirror failure cannot roll it back (ADR 0002)
        UserDto changed = QuarkusTransaction.requiringNew().call(() -> {
            UserEntity actor = userRepository.findByIdOptional(actorId)
                    .orElseThrow(() -> new ForbiddenRoleChangeException("Unknown actor: " + actorId));
            RoleEnum actorRole = actor.getRole();
            if (actorId.equals(personId))
                throw new ForbiddenRoleChangeException("Nobody changes their own Role.");

            UserEntity person = userRepository.findByIdOptional(personId)
                    .orElseThrow(() -> new UnknownResourceException("User not found: " + personId));
            if (!actorRole.maySetRole(person.getRole(), role))
                throw new ForbiddenRoleChangeException(actorRole.id() + " cannot set " + person.getRole().id() + " to " + role.id());

            SectorEntity sector = sectorAfter(actor, person, role, sectorId);
            boolean changesSector = person.getSector() != null && !person.belongsTo(sector);

            person.changeRole(role);
            person.setSector(sector);
            // A Chef's Affectation ends with the title, or when they leave their Groupe's Secteur
            if (!role.canLeadGroupe() || changesSector)
                internalGroupService.doEndAffectationOf(personId);
            return userMapper.toDto(person);
        });

        try {
            keycloakRoleMirror.setRoleGroup(personId, role);
        } catch (RuntimeException e) {
            // ADR 0002: the database Role still applies; the group is repaired on the next Role change
            Log.warnf(e, "Could not mirror Role %s of %s to Keycloak", role.id(), personId);
        }
        return changed;
    }

    /**
     * The person's Secteur once their Role changes (ADR 0004). Below the Super admin, the actor acts only on
     * Bénévoles and on their own Secteur's people, and gives their own Secteur. The Super admin names it when
     * appointing an Admin or giving a first Secteur. A Bénévole belongs to none.
     */
    private SectorEntity sectorAfter(UserEntity actor, UserEntity person, RoleEnum role, UUID sectorId) {
        if (actor.getRole() != RoleEnum.SUPER_ADMIN) {
            if (sectorId != null)
                throw new ForbiddenRoleChangeException("Only the Super admin names a Secteur.");
            if (actor.getSector() == null)
                throw new ForbiddenRoleChangeException(actor.getUserId() + " belongs to no Secteur.");
            if (person.getSector() != null && !person.belongsTo(actor.getSector()))
                throw new ForbiddenRoleChangeException(person.getUserId() + " belongs to another Secteur.");
            return role.isAtLeast(RoleEnum.MEMBRE) ? actor.getSector() : null;
        }
        if (!role.isAtLeast(RoleEnum.MEMBRE)) return null;
        boolean namesSector = role == RoleEnum.ADMIN || person.getSector() == null;
        if (!namesSector) {
            if (sectorId != null && !sectorId.equals(person.getSector().getSectorId()))
                throw new InvalidResourceException("Only appointing an Admin changes a person's Secteur.");
            return person.getSector();
        }
        if (sectorId == null)
            throw new InvalidResourceException("Name the Secteur of " + person.getUserId() + ".");
        SectorEntity sector = internalSectorService.doGetEntityById(sectorId)
                .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));
        if (sector.isClosed())
            throw new InvalidResourceException("A Secteur fermé gets nobody: " + sectorId);
        return sector;
    }

    @Override
    @Transactional
    public UserDto updateProfile(UUID personId, ProfileDto profile) {
        UserEntity person = userRepository.findByIdOptional(personId)
                .orElseThrow(() -> new UnknownResourceException("User not found: " + personId));
        if (isBlank(profile.firstName()) || isBlank(profile.lastName()))
            throw new InvalidResourceException("A profile has a first and a last name.");

        person.setFirstName(profile.firstName().trim());
        person.setLastName(profile.lastName().trim());
        person.setPhone(isBlank(profile.phone()) ? null : profile.phone().trim());
        return userMapper.toDto(person);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    @Override
    @Transactional
    public UserDto appointPresident(UUID personId) {
        UserEntity person = userRepository.findByIdOptional(personId)
                .orElseThrow(() -> new UnknownResourceException("User not found: " + personId));
        if (person.getRole() != RoleEnum.BUREAU)
            throw new InvalidResourceException("Only a Bureau member can be Président: " + personId);

        userRepository.findPresidents().forEach(previous -> previous.setPresident(false));
        person.setPresident(true);
        return userMapper.toDto(person);
    }

    @Override
    @Transactional
    public UserDto doCreate(UserDto userDto) {
        if (userRepository.existsById(userDto.getUserId()))
                throw new DuplicateResourceException("This resource already exists in the system.");

        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setRole(RoleEnum.BENEVOLE);
        userRepository.persist(userEntity);

        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto doUpdate(UserDto userDto) {
        UserEntity userEntity = userRepository.findByIdOptional(userDto.getUserId())
                .orElseThrow(() -> new UnknownResourceException("This resource is unknown in the system and cannot be updated."));

        userEntity = userMapper.partialDtoToEntity(userEntity,userDto);
        userRepository.persist(userEntity);
        return userMapper.toDto(userEntity);
    }


    //Todo: il va falloir par principe permettre la suppression d'un user. Nous devons permettre à chacun de supprimer ses traces.
    @Override
    @Transactional
    public void doDelete(UUID userId) {
        // On peut cependant imaginer tester si le user est utilisé dans une autre table et le supprimer dans le cas contraire
        //userRepository.deleteById(userId);
        throw new NotImplementedYetException(this.getClass().getSimpleName());
    }

    @Override
    public Optional<UserEntity> doGetEntityById(UUID userId) {
        return userRepository.findByIdOptional(userId);
    }

}
