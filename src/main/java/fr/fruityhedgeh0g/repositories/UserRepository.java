package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.model.entities.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

}
