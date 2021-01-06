package com.bw.coupon.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JacksonUtil {
    private final ObjectMapper objectMapper;
    @Autowired
    public JacksonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String writeValueAsString(Object object){
        try{
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e){
            log.error("JacksonUtil writeValueAsString Error: JsonProcessingException");
            return null;
        }
    }

    public <T> T readValue(String json, Class<T> valueType){
        try{
            return objectMapper.readValue(json,valueType);
        } catch (IOException e){
            log.error("JacksonUtil writeValueAsString Error: IOException");
            return null;
        }
    }
}
