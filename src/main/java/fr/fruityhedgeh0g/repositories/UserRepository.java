package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.UserEntity;
import fr.fruityhedgeh0g.model.entities.configurations.ConfigurationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.vertx.ext.auth.impl.jose.JWT;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {

//    public Optional<UserEntity> findByName(String token) {
//        return Optional.ofNullable(find("userId",token)
//                .firstResult());
//    }
}
