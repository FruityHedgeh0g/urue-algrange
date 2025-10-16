package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.entities.UserEntity;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import io.vertx.ext.auth.impl.jose.JWT;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.keycloak.representations.JsonWebToken;

@AllArgsConstructor
@ApplicationScoped
public class UserService {


    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

}
