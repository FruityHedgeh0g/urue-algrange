package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@ApplicationScoped
public class UserService {

    @Inject UserRepository userRepository;

    @Inject UserMapper userMapper;
}
