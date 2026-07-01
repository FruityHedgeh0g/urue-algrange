package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {

    public boolean existsById(UUID userId){
        return count("userId", userId) > 0;
    }

    public boolean existsByRole(UUID roleId){
        return count("roles.roleId", roleId) > 0;
    }

    public List<UserEntity> findByRole(UUID roleIds){
        return find("roles.roleId in ?1", roleIds).list();
    }
//    public Optional<UserEntity> findByName(String token) {
//        return Optional.ofNullable(find("userId",token)
//                .firstResult());
//    }
}
