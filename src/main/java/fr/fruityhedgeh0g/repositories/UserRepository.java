package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.vertx.ext.auth.impl.jose.JWT;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

//    public Optional<UserEntity> findByName(String token) {
//        return Optional.ofNullable(find("userId",token)
//                .firstResult());
//    }
}
