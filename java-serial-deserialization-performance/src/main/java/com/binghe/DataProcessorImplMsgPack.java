package com.binghe;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DataProcessorImplMsgPack implements DataProcessor {

    private static final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.findAndRegisterModules();
    }

    @Override
    public byte[] getBytes(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getObject(byte[] data, Class<T> tClass) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try {
            return objectMapper.readValue(bais, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
