package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {

    public boolean existsById(UUID userId){
        return count("userId", userId) > 0;
    }
//    public Optional<UserEntity> findByName(String token) {
//        return Optional.ofNullable(find("userId",token)
//                .firstResult());
//    }
}
