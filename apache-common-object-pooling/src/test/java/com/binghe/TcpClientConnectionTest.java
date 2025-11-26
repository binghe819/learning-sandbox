package com.binghe;

import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TcpClientConnectionTest {

    private static final String TEST_HOST = "localhost";
    private static final int CONNECT_TIMEOUT_MS = 3000;
    private static final int SOCKET_READ_TIMEOUT_MS = 2000;

    private TestStringUpperServer testStringUpperServer;
    private int serverPort;

    @BeforeEach
    void setUp() throws IOException {
        testStringUpperServer = new TestStringUpperServer();
        serverPort = testStringUpperServer.start();
    }

    @AfterEach
    void tearDown() {
        if (testStringUpperServer != null) {
            testStringUpperServer.stop();
        }
    }

    @Test
    @DisplayName("TCP 연결을 성공적으로 생성할 수 있다")
    void createConnection() throws IOException {
        // when
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);

        // then
        assertNotNull(connection);
        assertTrue(connection.isValid());

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("연결할 수 없는 포트로 연결 시도 시 예외가 발생한다")
    void connectionToInvalidPortFails() {
        // when & then - 사용되지 않는 포트로 연결 시도
        assertThrows(IOException.class, () -> {
            new TcpClientConnection(TEST_HOST, 54321, 500, SOCKET_READ_TIMEOUT_MS);
        });
    }

    @Test
    @DisplayName("바이트 배열을 소켓에 쓰면 대문자로 변환되어 돌아온다")
    void writeByteArray() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);
        byte[] data = "Hello, World!".getBytes("UTF-8");
        byte[] expected = "HELLO, WORLD!".getBytes("UTF-8");

        // when
        connection.write(data);
        byte[] response = connection.read(1024);

        // then
        assertArrayEquals(expected, response);

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("문자열을 소켓에 쓰면 대문자로 변환되어 돌아온다")
    void writeString() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);
        String message = "hello tcp";
        String expected = "HELLO TCP";

        // when
        connection.write(message);
        byte[] response = connection.read(1024);

        // then
        assertEquals(expected, new String(response, "UTF-8"));

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("null 또는 빈 바이트 배열은 아무것도 쓰지 않는다")
    void writeNullOrEmptyByteArray() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);

        // when & then - 예외가 발생하지 않아야 함
        assertDoesNotThrow(() -> connection.write((byte[]) null));
        assertDoesNotThrow(() -> connection.write(new byte[0]));

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("null 또는 빈 문자열은 아무것도 쓰지 않는다")
    void writeNullOrEmptyString() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);

        // when & then - 예외가 발생하지 않아야 함
        assertDoesNotThrow(() -> connection.write((String) null));
        assertDoesNotThrow(() -> connection.write(""));

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("버퍼에 데이터를 읽을 수 있다")
    void readIntoBuffer() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);
        connection.write("test data"); // 서버에 데이터 전송

        // when
        byte[] buffer = new byte[1024];
        int bytesRead = connection.read(buffer);

        // then
        assertTrue(bytesRead > 0);
        assertEquals("TEST DATA", new String(buffer, 0, bytesRead, "UTF-8"));

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("버퍼의 특정 오프셋에 데이터를 읽을 수 있다")
    void readIntoBufferWithOffset() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);
        connection.write("hello"); // 서버에 데이터 전송

        // when
        byte[] buffer = new byte[1024];
        int offset = 10;
        int bytesRead = connection.read(buffer, offset, 100);

        // then
        assertTrue(bytesRead > 0);
        assertEquals("HELLO", new String(buffer, offset, bytesRead, "UTF-8"));

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("큰 데이터를 전송하면 대문자로 변환되어 돌아온다")
    void writeLargeData() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("large data chunk ").append(i).append(" ");
        }
        byte[] largeData = sb.toString().getBytes("UTF-8");
        byte[] expectedData = sb.toString().toUpperCase().getBytes("UTF-8");

        // when
        connection.write(largeData);
        byte[] buffer = new byte[expectedData.length];
        int totalRead = 0;
        while (totalRead < expectedData.length) {
            int bytesRead = connection.read(buffer, totalRead, expectedData.length - totalRead);
            totalRead += bytesRead;
        }

        // then
        assertArrayEquals(expectedData, buffer);

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("연결을 닫으면 isValid가 false를 반환한다")
    void closeConnection() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);
        assertTrue(connection.isValid());

        // when
        connection.close();

        // then
        assertFalse(connection.isValid());
    }

    @Test
    @DisplayName("연결이 닫힌 후 read/write 시도 시 예외가 발생한다")
    void operationsOnClosedConnection() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);
        connection.close();

        // when & then
        assertThrows(IOException.class, () -> connection.write("test"));
        assertThrows(IOException.class, () -> connection.read(1024));
    }

    @Test
    @DisplayName("서버가 연결을 닫으면 IOException이 발생한다")
    void readFromClosedServerConnection() throws IOException, InterruptedException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);
        
        // when - 서버를 종료
        testStringUpperServer.stop();
        Thread.sleep(100); // 연결이 닫힐 시간을 줌

        // then - read 시도 시 IOException 발생
        assertThrows(IOException.class, () -> connection.read(1024));

        // cleanup
        connection.close();
        testStringUpperServer = null; // tearDown에서 다시 stop하지 않도록
    }

    @Test
    @DisplayName("여러 메시지를 순차적으로 전송하면 대문자로 변환되어 돌아온다")
    void sendMultipleMessages() throws IOException {
        // given
        TcpClientConnection connection = new TcpClientConnection(TEST_HOST, serverPort, CONNECT_TIMEOUT_MS, SOCKET_READ_TIMEOUT_MS);

        // when & then
        for (int i = 0; i < 10; i++) {
            String message = "message " + i;
            String expected = "MESSAGE " + i;
            connection.write(message);
            byte[] response = connection.read(1024);
            assertEquals(expected, new String(response, "UTF-8"));
        }

        // cleanup
        connection.close();
    }
}

