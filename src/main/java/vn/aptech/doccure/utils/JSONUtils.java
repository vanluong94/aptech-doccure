package vn.aptech.doccure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {

    public static String stringify(Object object) {
        // Create ObjectMapper object.
        ObjectMapper mapper = new ObjectMapper();
        // Serialize Object to JSON.
        String json = null;
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

}
