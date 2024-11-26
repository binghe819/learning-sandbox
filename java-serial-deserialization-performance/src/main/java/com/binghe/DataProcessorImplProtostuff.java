package com.binghe;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataProcessorImplProtostuff implements DataProcessor {

    static IdStrategy ID_STRATEGY = new DefaultIdStrategy(getFlag(), null, 0);

    @SuppressWarnings("rawtypes")
    private final Map<String, Schema> schemaMap = new ConcurrentHashMap<>();

    public static int getFlag() {

        return IdStrategy.DEFAULT_FLAGS
                | IdStrategy.PRESERVE_NULL_ELEMENTS
                | IdStrategy.MORPH_COLLECTION_INTERFACES
                | IdStrategy.MORPH_MAP_INTERFACES
                | IdStrategy.MORPH_NON_FINAL_POJOS;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public byte[] getBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        Schema schema = getSchma(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] b;
        try {
            b = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return b;
    }

    @SuppressWarnings("unchecked")
    private <T> Schema<T> getSchma(Class<T> tClass) {
        Schema<T> schema = schemaMap.get(tClass.getName());
        if (schema == null) {
            schema = RuntimeSchema.getSchema(tClass, ID_STRATEGY);
            schemaMap.put(tClass.getName(), schema);
        }
        return schema;
    }

    @Override
    public <T> T getObject(byte[] data, Class<T> tClass) {
        if (data == null) {
            return null;
        }
        Schema<T> schema = getSchma(tClass);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }
}
