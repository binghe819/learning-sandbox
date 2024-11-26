package com.binghe;

public interface DataCompressor {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
