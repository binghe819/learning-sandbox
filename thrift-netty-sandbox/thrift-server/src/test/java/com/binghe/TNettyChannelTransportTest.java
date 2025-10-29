package com.binghe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.thrift.TConfiguration;
import org.apache.thrift.transport.TTransportException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TNettyChannelTransportTest {

    private ByteBuf readBuffer;
    private ByteBuf writeBuffer;
    private TNettyChannelTransport transport;

    @BeforeEach
    void setUp() {
        readBuffer = Unpooled.buffer();
        writeBuffer = Unpooled.buffer();
        transport = new TNettyChannelTransport(readBuffer, writeBuffer);
    }

    @AfterEach
    void tearDown() {
        if (readBuffer != null && readBuffer.refCnt() > 0) {
            readBuffer.release();
        }
        if (writeBuffer != null && writeBuffer.refCnt() > 0) {
            writeBuffer.release();
        }
    }

    @Test
    @DisplayName("생성자 - 두 개의 ByteBuf로 생성")
    void testConstructorWithTwoBuffers() {
        assertNotNull(transport);
        assertNotNull(transport.getConfiguration());
    }

    @Test
    @DisplayName("생성자 - TConfiguration과 함께 생성")
    void testConstructorWithConfiguration() {
        TConfiguration config = new TConfiguration();
        TNettyChannelTransport customTransport = new TNettyChannelTransport(readBuffer, writeBuffer, config);
        
        assertNotNull(customTransport);
        assertEquals(config, customTransport.getConfiguration());
        customTransport.close();
    }

    @Test
    @DisplayName("생성자 - null configuration은 기본 configuration으로 대체")
    void testConstructorWithNullConfiguration() {
        TNettyChannelTransport customTransport = new TNettyChannelTransport(readBuffer, writeBuffer, null);
        
        assertNotNull(customTransport.getConfiguration());
        customTransport.close();
    }

    @Test
    @DisplayName("isOpen - 버퍼가 모두 유효하면 true 반환")
    void testIsOpen_WhenBuffersAreValid() {
        assertTrue(transport.isOpen());
    }

    @Test
    @DisplayName("isOpen - readBuffer가 release되면 false 반환")
    void testIsOpen_WhenReadBufferReleased() {
        readBuffer.release();
        
        assertFalse(transport.isOpen());
    }

    @Test
    @DisplayName("isOpen - writeBuffer가 release되면 false 반환")
    void testIsOpen_WhenWriteBufferReleased() {
        writeBuffer.release();
        
        assertFalse(transport.isOpen());
    }

    @Test
    @DisplayName("isOpen - null 버퍼가 있으면 false 반환")
    void testIsOpen_WhenBuffersAreNull() {
        TNettyChannelTransport nullTransport = new TNettyChannelTransport(null, null);
        
        assertFalse(nullTransport.isOpen());
        nullTransport.close();
    }

    @Test
    @DisplayName("open - 예외 없이 실행됨")
    void testOpen() {
        assertDoesNotThrow(() -> transport.open());
    }

    @Test
    @DisplayName("close - 예외 없이 실행됨")
    void testClose() {
        assertDoesNotThrow(() -> transport.close());
    }

    @Test
    @DisplayName("read - 정상적으로 데이터 읽기")
    void testRead_Successfully() throws TTransportException {
        // Given: readBuffer에 데이터 쓰기
        byte[] sourceData = "Hello World".getBytes();
        readBuffer.writeBytes(sourceData);
        
        // When: 데이터 읽기
        byte[] destination = new byte[11];
        int bytesRead = transport.read(destination, 0, 11);
        
        // Then
        assertEquals(11, bytesRead);
        assertArrayEquals(sourceData, destination);
    }

    @Test
    @DisplayName("read - 요청한 길이보다 적은 데이터만 있을 때")
    void testRead_WhenLessDataAvailable() throws TTransportException {
        // Given: 5바이트만 준비
        byte[] sourceData = "Hello".getBytes();
        readBuffer.writeBytes(sourceData);
        
        // When: 10바이트 요청
        byte[] destination = new byte[10];
        int bytesRead = transport.read(destination, 0, 10);
        
        // Then: 5바이트만 읽힘
        assertEquals(5, bytesRead);
    }

    @Test
    @DisplayName("read - 읽을 데이터가 없을 때 0 반환")
    void testRead_WhenNoDataAvailable() throws TTransportException {
        // Given: readBuffer가 비어있음
        
        // When
        byte[] destination = new byte[10];
        int bytesRead = transport.read(destination, 0, 10);
        
        // Then
        assertEquals(0, bytesRead);
    }

    @Test
    @DisplayName("read - readBuffer가 null일 때 0 반환")
    void testRead_WhenBufferIsNull() throws TTransportException {
        TNettyChannelTransport nullTransport = new TNettyChannelTransport(null, writeBuffer);
        
        byte[] destination = new byte[10];
        int bytesRead = nullTransport.read(destination, 0, 10);
        
        assertEquals(0, bytesRead);
        nullTransport.close();
    }

    @Test
    @DisplayName("write - 정상적으로 데이터 쓰기")
    void testWrite_Successfully() throws TTransportException {
        // Given
        byte[] data = "Hello Thrift".getBytes();
        
        // When
        transport.write(data, 0, data.length);
        
        // Then
        assertEquals(data.length, writeBuffer.readableBytes());
        
        byte[] written = new byte[data.length];
        writeBuffer.readBytes(written);
        assertArrayEquals(data, written);
    }

    @Test
    @DisplayName("write - offset과 length를 사용하여 부분 쓰기")
    void testWrite_WithOffsetAndLength() throws TTransportException {
        // Given
        byte[] data = "Hello Thrift".getBytes();
        
        // When: "Thrift"만 쓰기 (offset=6, length=6)
        transport.write(data, 6, 6);
        
        // Then
        assertEquals(6, writeBuffer.readableBytes());
        
        byte[] written = new byte[6];
        writeBuffer.readBytes(written);
        assertArrayEquals("Thrift".getBytes(), written);
    }

    @Test
    @DisplayName("write - writeBuffer가 null일 때 예외 발생")
    void testWrite_WhenBufferIsNull() {
        TNettyChannelTransport nullTransport = new TNettyChannelTransport(readBuffer, null);
        byte[] data = "test".getBytes();
        
        TTransportException exception = assertThrows(
            TTransportException.class,
            () -> nullTransport.write(data, 0, data.length)
        );
        
        assertTrue(exception.getMessage().contains("Write buffer is null"));
        nullTransport.close();
    }

    @Test
    @DisplayName("readAll - 정상적으로 모든 데이터 읽기")
    void testReadAll_Successfully() throws TTransportException {
        // Given
        byte[] sourceData = "Test Data".getBytes();
        readBuffer.writeBytes(sourceData);
        
        // When
        byte[] destination = new byte[9];
        int bytesRead = transport.readAll(destination, 0, 9);
        
        // Then
        assertEquals(9, bytesRead);
        assertArrayEquals(sourceData, destination);
    }

    @Test
    @DisplayName("readAll - 데이터가 부족하면 예외 발생")
    void testReadAll_WhenNotEnoughData() {
        // Given: 5바이트만 준비
        byte[] sourceData = "Hello".getBytes();
        readBuffer.writeBytes(sourceData);
        
        // When & Then: 10바이트 요청하면 예외 발생
        byte[] destination = new byte[10];
        TTransportException exception = assertThrows(
            TTransportException.class,
            () -> transport.readAll(destination, 0, 10)
        );
        
        assertEquals(TTransportException.END_OF_FILE, exception.getType());
        assertTrue(exception.getMessage().contains("Not enough bytes in buffer"));
    }

    @Test
    @DisplayName("readAll - readBuffer가 null일 때 예외 발생")
    void testReadAll_WhenBufferIsNull() {
        TNettyChannelTransport nullTransport = new TNettyChannelTransport(null, writeBuffer);
        byte[] destination = new byte[10];
        
        TTransportException exception = assertThrows(
            TTransportException.class,
            () -> nullTransport.readAll(destination, 0, 10)
        );
        
        assertTrue(exception.getMessage().contains("Read buffer is null"));
        nullTransport.close();
    }

    @Test
    @DisplayName("flush - 예외 없이 실행됨")
    void testFlush() {
        assertDoesNotThrow(() -> transport.flush());
    }

    @Test
    @DisplayName("getConfiguration - 설정 객체 반환")
    void testGetConfiguration() {
        TConfiguration config = transport.getConfiguration();
        
        assertNotNull(config);
    }

    @Test
    @DisplayName("updateKnownMessageSize - 메시지 크기 업데이트")
    void testUpdateKnownMessageSize() throws TTransportException {
        // When
        transport.updateKnownMessageSize(1024);
        
        // Then: 예외 없이 실행되어야 함
        assertDoesNotThrow(() -> transport.updateKnownMessageSize(2048));
    }

    @Test
    @DisplayName("updateKnownMessageSize - configuration이 null이 아닐 때 정상 실행")
    void testUpdateKnownMessageSize_WithConfiguration() {
        assertDoesNotThrow(() -> transport.updateKnownMessageSize(1024));
    }

    @Test
    @DisplayName("checkReadBytesAvailable - 충분한 바이트가 있을 때 예외 없음")
    void testCheckReadBytesAvailable_WhenEnoughBytes() {
        // Given: 10바이트 준비
        byte[] data = "1234567890".getBytes();
        readBuffer.writeBytes(data);
        
        // When & Then: 10바이트 체크는 성공
        assertDoesNotThrow(() -> transport.checkReadBytesAvailable(10));
    }

    @Test
    @DisplayName("checkReadBytesAvailable - 바이트가 부족하면 예외 발생")
    void testCheckReadBytesAvailable_WhenNotEnoughBytes() {
        // Given: 5바이트만 준비
        byte[] data = "12345".getBytes();
        readBuffer.writeBytes(data);
        
        // When & Then: 10바이트 체크는 실패
        TTransportException exception = assertThrows(
            TTransportException.class,
            () -> transport.checkReadBytesAvailable(10)
        );
        
        assertEquals(TTransportException.END_OF_FILE, exception.getType());
        assertTrue(exception.getMessage().contains("Not enough readable bytes"));
    }

    @Test
    @DisplayName("checkReadBytesAvailable - readBuffer가 null일 때 예외 발생")
    void testCheckReadBytesAvailable_WhenBufferIsNull() {
        TNettyChannelTransport nullTransport = new TNettyChannelTransport(null, writeBuffer);
        
        TTransportException exception = assertThrows(
            TTransportException.class,
            () -> nullTransport.checkReadBytesAvailable(10)
        );
        
        assertTrue(exception.getMessage().contains("Read buffer is null"));
        nullTransport.close();
    }

    @Test
    @DisplayName("통합 테스트 - 읽기와 쓰기를 함께 사용")
    void testIntegration_ReadAndWrite() throws TTransportException {
        // Given: readBuffer에 요청 데이터 준비
        byte[] request = "REQUEST".getBytes();
        readBuffer.writeBytes(request);
        
        // When: 요청 읽기
        byte[] readData = new byte[7];
        int bytesRead = transport.readAll(readData, 0, 7);
        
        // And: 응답 쓰기
        byte[] response = "RESPONSE".getBytes();
        transport.write(response, 0, response.length);
        
        // Then
        assertEquals(7, bytesRead);
        assertArrayEquals(request, readData);
        assertEquals(8, writeBuffer.readableBytes());
        
        byte[] writtenData = new byte[8];
        writeBuffer.readBytes(writtenData);
        assertArrayEquals(response, writtenData);
    }
}