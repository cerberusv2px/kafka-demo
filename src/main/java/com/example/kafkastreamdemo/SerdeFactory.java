package com.example.kafkastreamdemo;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class SerdeFactory {
    public static <T> Serde<T> createSerdeFor(Class<T> clazz, boolean isKey) {
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put("Class", clazz);

        Serializer<T> ser = new JsonSerializer<>();
        ser.configure(serdeProps, isKey);

        Deserializer<T> de = new JsonDeserializer<>();
        de.configure(serdeProps, isKey);

        return Serdes.serdeFrom(ser, de);
    }
}
