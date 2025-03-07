package org.saltaonelove.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JsonLoader {
    private static final Logger log = LoggerFactory.getLogger(JsonLoader.class);
    private final ObjectMapper objectMapper;

    public JsonLoader() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> List<T> loadFromJson(String fileName, TypeReference<List<T>> valueType) {
        log.info("Attempting to load JSON file: {}", fileName);

        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            log.info("Found resource: {} (exists: {})", fileName, resource.exists());

            List<T> data = objectMapper.readValue(resource.getInputStream(), valueType);
            log.info("Successfully loaded {} records from {}", (data != null ? data.size() : 0), fileName);

            return data;
        } catch (IOException e) {
            log.error("Error loading JSON file: {}", fileName, e);
            return null;
        }
    }
}
