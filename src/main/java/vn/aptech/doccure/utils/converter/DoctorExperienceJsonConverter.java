package vn.aptech.doccure.utils.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.aptech.doccure.model.DoctorExperience;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class DoctorExperienceJsonConverter implements AttributeConverter<List<DoctorExperience>, String> {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(DoctorExperienceJsonConverter.class);

    @Override
    public String convertToDatabaseColumn(List<DoctorExperience> list) {

        String json = null;
        try {
            json = jsonMapper.writeValueAsString(list);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return json;
    }

    @Override
    public List<DoctorExperience> convertToEntityAttribute(String json) {
        List<DoctorExperience> list = null;
        try {
            list = jsonMapper.readValue(json, new TypeReference<List<DoctorExperience>>() {
            });
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }

        return list;
    }
}

