package fr.fruityhedgeh0g.dtos.eventDtos;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupRefDto;
import fr.fruityhedgeh0g.entities.EventRegistrationEntity;
import fr.fruityhedgeh0g.enums.DemandeStatusEnum;

/** The latest Demande de groupe of a sign-up: the Groupe asked for and where it stands. */
public record DemandeDto(GroupRefDto group, DemandeStatusEnum status) {
    public static DemandeDto of(EventRegistrationEntity registration) {
        return registration.getDemandeStatus() == null ? null
                : new DemandeDto(GroupRefDto.of(registration.getDemandeGroup()), registration.getDemandeStatus());
    }
}
