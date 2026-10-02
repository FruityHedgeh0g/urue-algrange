package fr.fruityhedgeh0g.queues.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.fruityhedgeh0g.dtos.sectorDtos.NestedSectorDto;
import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
// Read outside any REST endpoint, so the native image would not keep their constructors otherwise
@RegisterForReflection(targets = {UserDto.class, NestedSectorDto.class})
public class KeycloakDeserializer {

    @Inject
    ObjectMapper objectMapper;

    public UserDto deserializePayloadToUserDto(String payload) {
        try {
            return objectMapper.readValue(payload, UserDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize user payload", e);
        }
    }
}
