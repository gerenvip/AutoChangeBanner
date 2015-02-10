package com.gerenvip.banner.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JacksonUtil {

    private JacksonUtil() {

    }

    private static ObjectMapper objectMapper=new ObjectMapper();

    static{
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    @SuppressWarnings({
            "unchecked"
    })
    public static Map<String, String> readValue(String respons) {
// respons = "{\"userName\": \"a\",\"password\":\"c\"}";
        try {
            return objectMapper.readValue(respons, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<String, String>();
    }

    public static <T> T readValue(String response, Class<T> valueType) {
        try {
            return objectMapper.readValue(response, valueType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
