package vn.aptech.doccure.utils.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class ImagesJsonConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(ImagesJsonConverter.class);

    @Override
    public String convertToDatabaseColumn(List<String> list) {

        String json = null;
        try {
            json = jsonMapper.writeValueAsString(list);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return json;
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        List<String> list = null;
        try {
            list = jsonMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }

        return list;
    }

}
