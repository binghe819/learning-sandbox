package com.binghe;

import com.binghe.thrift.LargeObject;
import com.binghe.thrift.SmallObject;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.transport.TTransportException;

public class DataProcessorImplTProtocol {

    private static final TSerializer serializer;
    private static final TDeserializer deserializer;

    static {
        try {
            serializer = new TSerializer();
            deserializer = new TDeserializer();
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encodeSmallObject(SmallObject smallObject) {
        try {
            return serializer.serialize(smallObject);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public SmallObject decodeSmallObject(byte[] data) {
        try {
            SmallObject smallObject = new SmallObject();
            deserializer.deserialize(smallObject, data);
            return smallObject;
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encodeLargeObject(LargeObject largeObject) {
        try {
            return serializer.serialize(largeObject);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public LargeObject decodeLargeObject(byte[] data) {
        try {
            LargeObject largeObject = new LargeObject();
            deserializer.deserialize(largeObject, data);
            return largeObject;
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }
}
