package com.example.kafkastreamdemo;

import java.util.Map;

import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class IdDeserializer implements Deserializer<String> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> props, boolean isKey) { }

    @Override
    public void close() { }

    @Override
    public String deserialize(String topic, byte[] bytes) {
        if (bytes == null)
            return null;

        String id;
        try {
            Map payload = objectMapper.readValue(new String(bytes), Map.class);
            id = String.valueOf(payload.get("id"));
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return id;
    }
}
