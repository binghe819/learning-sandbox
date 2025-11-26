package com.binghe;

import org.apache.commons.pool2.PooledObject;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClientConnectionFactoryTest {

    private static final String TEST_HOST = "localhost";
    private static final int CONNECT_TIMEOUT_MS = 3000;
    private static final int SOCKET_READ_TIMEOUT_MS = 2000;

    private TestStringUpperServer testStringUpperServer;
    private int serverPort;
    private ClientConnectionFactory factory;

    @BeforeEach
    void setUp() throws IOException {
        testStringUpperServer = new TestStringUpperServer();
        serverPort = testStringUpperServer.start();

        PoolConfig poolConfig = PoolConfig.builder()
                .host(TEST_HOST)
                .port(serverPort)
                .connectionTimeoutMillis(CONNECT_TIMEOUT_MS)
                .socketTimeoutMillis(SOCKET_READ_TIMEOUT_MS)
                .build();

        factory = new ClientConnectionFactory(poolConfig);
    }

    @AfterEach
    void tearDown() {
        if (testStringUpperServer != null) {
            testStringUpperServer.stop();
        }
    }

    @Test
    @DisplayName("커넥션 객체를 성공적으로 생성할 수 있다")
    void createConnection() throws Exception {
        // when
        TcpClientConnection connection = factory.create();

        // then
        assertNotNull(connection);
        assertTrue(connection.isValid());

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("생성된 커넥션을 PooledObject로 래핑할 수 있다")
    void wrapConnection() throws Exception {
        // given
        TcpClientConnection connection = factory.create();

        // when
        PooledObject<TcpClientConnection> pooledObject = factory.wrap(connection);

        // then
        assertNotNull(pooledObject);
        assertSame(connection, pooledObject.getObject());

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("유효한 커넥션은 검증을 통과한다")
    void validateValidConnection() throws Exception {
        // given
        TcpClientConnection connection = factory.create();
        PooledObject<TcpClientConnection> pooledObject = factory.wrap(connection);

        // when
        boolean isValid = factory.validateObject(pooledObject);

        // then
        assertTrue(isValid);

        // cleanup
        connection.close();
    }

    @Test
    @DisplayName("닫힌 커넥션은 검증을 실패한다")
    void validateClosedConnection() throws Exception {
        // given
        TcpClientConnection connection = factory.create();
        PooledObject<TcpClientConnection> pooledObject = factory.wrap(connection);
        connection.close();

        // when
        boolean isValid = factory.validateObject(pooledObject);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("null 커넥션은 검증을 실패한다")
    void validateNullConnection() {
        // given
        PooledObject<TcpClientConnection> pooledObject = factory.wrap(null);

        // when
        boolean isValid = factory.validateObject(pooledObject);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("커넥션 객체를 파괴할 수 있다")
    void destroyConnection() throws Exception {
        // given
        TcpClientConnection connection = factory.create();
        PooledObject<TcpClientConnection> pooledObject = factory.wrap(connection);
        assertTrue(connection.isValid());

        // when
        factory.destroyObject(pooledObject);

        // then
        assertFalse(connection.isValid());
    }

    @Test
    @DisplayName("null 커넥션 파괴 시 예외가 발생하지 않는다")
    void destroyNullConnection() {
        // given
        PooledObject<TcpClientConnection> pooledObject = factory.wrap(null);

        // when & then
        assertDoesNotThrow(() -> factory.destroyObject(pooledObject));
    }

    @Test
    @DisplayName("서버가 없는 경우 커넥션 생성 시 예외가 발생한다")
    void createConnectionWithoutServer() throws Exception {
        // given - 서버 종료
        testStringUpperServer.stop();
        Thread.sleep(100);

        // when & then
        assertThrows(Exception.class, () -> factory.create());
    }

    @Test
    @DisplayName("여러 커넥션을 순차적으로 생성할 수 있다")
    void createMultipleConnections() throws Exception {
        // when
        TcpClientConnection connection1 = factory.create();
        TcpClientConnection connection2 = factory.create();
        TcpClientConnection connection3 = factory.create();

        // then
        assertNotNull(connection1);
        assertNotNull(connection2);
        assertNotNull(connection3);
        assertTrue(connection1.isValid());
        assertTrue(connection2.isValid());
        assertTrue(connection3.isValid());

        // cleanup
        connection1.close();
        connection2.close();
        connection3.close();
    }

    @Test
    @DisplayName("팩토리로 생성한 커넥션으로 데이터를 주고받으면 대문자로 변환되어 돌아온다")
    void connectionCanSendAndReceiveData() throws Exception {
        // given
        TcpClientConnection connection = factory.create();
        String testMessage = "hello from factory test";
        String expected = "HELLO FROM FACTORY TEST";

        // when
        connection.write(testMessage);
        byte[] response = connection.read(1024);

        // then
        assertEquals(expected, new String(response, "UTF-8"));

        // cleanup
        connection.close();
    }
}

