package fr.fruityhedgeh0g.queues.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
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
