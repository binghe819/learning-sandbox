package com.binghe.bytebuffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

public class ByteBufferTest {

    @Test
    void read_and_write() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(11);
        assertThat(byteBuffer.position()).isEqualTo(0);
        assertThat(byteBuffer.limit()).isEqualTo(11);

        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 2);
        byteBuffer.put((byte) 3);
        byteBuffer.put((byte) 4);
        assertThat(byteBuffer.position()).isEqualTo(4);
        assertThat(byteBuffer.limit()).isEqualTo(11);

        // The limit is set to the current position and then the position is set to zero. If the mark is defined then it is discarded
        byteBuffer.flip();
        assertThat(byteBuffer.position()).isEqualTo(0);
        assertThat(byteBuffer.limit()).isEqualTo(4);

        // get(index)는 position을 올리지 않는다.
        byteBuffer.get(3);
        assertThat(byteBuffer.position()).isEqualTo(0);

        // get()은 position의 값을 반환하고 position을 1만큼 올린다.
        byteBuffer.get();
        assertThat(byteBuffer.position()).isEqualTo(1);
    }
}
