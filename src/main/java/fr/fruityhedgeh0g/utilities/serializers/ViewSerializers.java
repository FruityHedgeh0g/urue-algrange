package fr.fruityhedgeh0g.utilities.serializers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import fr.fruityhedgeh0g.model.dtos.GlobalViews;

import java.io.IOException;

public class ViewSerializers extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithView(GlobalViews.IdentifierOnly.class).writeValue(jgen, value);
    }
}
