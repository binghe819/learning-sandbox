package com.binghe;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientConnectionPoolTest {

    private static final String TEST_HOST = "localhost";
    private static final int CONNECT_TIMEOUT_MS = 3000;
    private static final int SOCKET_READ_TIMEOUT_MS = 2000;

    private TestStringUpperServer testStringUpperServer;
    private int serverPort;
    private ClientConnectionPool pool;

    @BeforeEach
    void setUp() throws IOException {
        testStringUpperServer = new TestStringUpperServer();
        serverPort = testStringUpperServer.start();

        PoolConfig poolConfig = PoolConfig.builder()
                .host(TEST_HOST)
                .port(serverPort)
                .connectionTimeoutMillis(CONNECT_TIMEOUT_MS)
                .socketTimeoutMillis(SOCKET_READ_TIMEOUT_MS)
                .maxTotal(10)
                .maxIdle(5)
                .minIdle(2)
                .maxWaitMillis(2000)
                .build();

        ClientConnectionFactory factory = new ClientConnectionFactory(poolConfig);
        GenericObjectPool<TcpClientConnection> objectPool = new GenericObjectPool<>(factory, poolConfig.getPoolConfig());
        pool = new ClientConnectionPool(objectPool);
    }

    @AfterEach
    void tearDown() {
        if (pool != null && !pool.isClosed()) {
            pool.close();
        }
        if (testStringUpperServer != null) {
            testStringUpperServer.stop();
        }
    }

    @Test
    @DisplayName("풀에서 커넥션을 빌릴 수 있다")
    void borrowConnection() throws Exception {
        // when
        TcpClientConnection connection = pool.borrowConnection();

        // then
        assertNotNull(connection);
        assertTrue(connection.isValid());
        assertEquals(1, pool.getNumActive());

        // cleanup
        pool.returnConnection(connection);
    }

    @Test
    @DisplayName("빌린 커넥션을 반납할 수 있다")
    void returnConnection() throws Exception {
        // given
        TcpClientConnection connection = pool.borrowConnection();
        assertEquals(1, pool.getNumActive());

        // when
        pool.returnConnection(connection);

        // then
        assertEquals(0, pool.getNumActive());
        assertTrue(pool.getNumIdle() > 0);
    }

    @Test
    @DisplayName("null 커넥션 반납 시 예외가 발생하지 않는다")
    void returnNullConnection() {
        // when & then
        assertDoesNotThrow(() -> pool.returnConnection(null));
    }

    @Test
    @DisplayName("잘못된 커넥션을 무효화할 수 있다")
    void invalidateConnection() throws Exception {
        // given
        TcpClientConnection connection = pool.borrowConnection();
        int activeCountBefore = pool.getNumActive();

        // when
        pool.invalidateConnection(connection);

        // then
        assertEquals(activeCountBefore - 1, pool.getNumActive());
        assertFalse(connection.isValid());
    }

    @Test
    @DisplayName("null 커넥션 무효화 시 예외가 발생하지 않는다")
    void invalidateNullConnection() {
        // when & then
        assertDoesNotThrow(() -> pool.invalidateConnection(null));
    }

    @Test
    @DisplayName("여러 커넥션을 동시에 빌릴 수 있다")
    void borrowMultipleConnections() throws Exception {
        // when
        TcpClientConnection conn1 = pool.borrowConnection();
        TcpClientConnection conn2 = pool.borrowConnection();
        TcpClientConnection conn3 = pool.borrowConnection();

        // then
        assertNotNull(conn1);
        assertNotNull(conn2);
        assertNotNull(conn3);
        assertEquals(3, pool.getNumActive());

        // cleanup
        pool.returnConnection(conn1);
        pool.returnConnection(conn2);
        pool.returnConnection(conn3);
    }

    @Test
    @DisplayName("최대 커넥션 수 초과 시 대기한다")
    void maxConnectionsExceeded() throws Exception {
        // given - maxTotal = 10으로 설정되어 있음
        List<TcpClientConnection> connections = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            connections.add(pool.borrowConnection());
        }
        assertEquals(10, pool.getNumActive());

        // when - 11번째 커넥션 요청 (타임아웃 설정)
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<TcpClientConnection> future = executor.submit(() -> pool.borrowConnection());

        // then - maxWaitMillis(2000ms) 내에 타임아웃으로 예외 발생
        assertThrows(Exception.class, () -> {
            future.get(3, TimeUnit.SECONDS);
        });

        // cleanup
        executor.shutdownNow();
        for (TcpClientConnection conn : connections) {
            pool.returnConnection(conn);
        }
    }

    @Test
    @DisplayName("커넥션으로 데이터를 주고받으면 대문자로 변환되어 돌아온다")
    void sendAndReceiveData() throws Exception {
        // given
        TcpClientConnection connection = pool.borrowConnection();
        String testMessage = "hello from pool test";
        String expected = "HELLO FROM POOL TEST";

        // when
        connection.write(testMessage);
        byte[] response = connection.read(1024);

        // then
        assertEquals(expected, new String(response, "UTF-8"));

        // cleanup
        pool.returnConnection(connection);
    }

    @Test
    @DisplayName("executeWithConnection으로 자동 반납을 사용할 수 있다")
    void executeWithConnection() throws Exception {
        // given
        String testMessage = "auto return test";
        String expected = "AUTO RETURN TEST";

        // when
        String result = pool.executeWithConnection(connection -> {
            connection.write(testMessage);
            byte[] response = connection.read(1024);
            return new String(response, "UTF-8");
        });

        // then
        assertEquals(expected, result);
        assertEquals(0, pool.getNumActive());
        assertTrue(pool.getNumIdle() > 0);
    }

    @Test
    @DisplayName("executeWithConnection 예외 발생 시 커넥션이 무효화된다")
    void executeWithConnectionException() {
        // when & then
        assertThrows(Exception.class, () -> {
            pool.executeWithConnection(connection -> {
                throw new RuntimeException("Test exception");
            });
        });

        // 예외 발생 후에도 풀이 정상 동작
        assertEquals(0, pool.getNumActive());
    }

    @Test
    @DisplayName("executeWithConnection으로 다양한 작업을 수행할 수 있다")
    void executeWithConnectionMultipleTimes() throws Exception {
        // when & then
        for (int i = 0; i < 5; i++) {
            String message = "message " + i;
            String expected = "MESSAGE " + i;
            String result = pool.executeWithConnection(connection -> {
                connection.write(message);
                byte[] response = connection.read(1024);
                return new String(response, "UTF-8");
            });
            assertEquals(expected, result);
        }

        // 모든 작업 후 활성 커넥션이 없어야 함
        assertEquals(0, pool.getNumActive());
    }

    @Test
    @DisplayName("풀 통계를 확인할 수 있다")
    void getPoolStats() throws Exception {
        // given
        TcpClientConnection conn1 = pool.borrowConnection();
        TcpClientConnection conn2 = pool.borrowConnection();

        // when
        String stats = pool.getPoolStats();

        // then
        assertNotNull(stats);
        assertTrue(stats.contains("Active=2"));
        assertTrue(stats.contains("Closed=false"));

        // cleanup
        pool.returnConnection(conn1);
        pool.returnConnection(conn2);
    }

    @Test
    @DisplayName("풀을 clear하면 유휴 커넥션이 제거된다")
    void clearPool() throws Exception {
        // given
        TcpClientConnection connection = pool.borrowConnection();
        pool.returnConnection(connection);
        assertTrue(pool.getNumIdle() > 0);

        // when
        pool.clear();

        // then
        assertEquals(0, pool.getNumIdle());
    }

    @Test
    @DisplayName("풀을 close하면 모든 커넥션이 정리된다")
    void closePool() throws Exception {
        // given
        TcpClientConnection connection = pool.borrowConnection();
        pool.returnConnection(connection);
        assertFalse(pool.isClosed());

        // when
        pool.close();

        // then
        assertTrue(pool.isClosed());
    }

    @Test
    @DisplayName("동시에 여러 스레드에서 커넥션을 안전하게 사용할 수 있다")
    void concurrentConnectionUsage() throws Exception {
        // given
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Future<Boolean>> futures = new ArrayList<>();

        // when
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            Future<Boolean> future = executor.submit(() -> {
                try {
                    latch.countDown();
                    latch.await(); // 모든 스레드가 동시에 시작하도록

                    String message = "thread-" + index;
                    String expected = "THREAD-" + index;
                    String result = pool.executeWithConnection(connection -> {
                        connection.write(message);
                        byte[] response = connection.read(1024);
                        return new String(response, "UTF-8");
                    });

                    return expected.equals(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
            futures.add(future);
        }

        // then
        for (Future<Boolean> future : futures) {
            assertTrue(future.get(10, TimeUnit.SECONDS));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // 모든 작업 후 활성 커넥션이 없어야 함
        assertEquals(0, pool.getNumActive());
    }

    @Test
    @DisplayName("풀에서 커넥션을 빌리고 반납하면 재사용 가능한 상태가 된다")
    void connectionPoolReusability() throws Exception {
        // when - 여러 번 빌리고 반납
        for (int i = 0; i < 5; i++) {
            TcpClientConnection connection = pool.borrowConnection();
            connection.write("test " + i);
            byte[] response = connection.read(1024);
            assertEquals("TEST " + i, new String(response, "UTF-8"));
            pool.returnConnection(connection);
        }

        // then - 풀이 정상적으로 동작함
        assertEquals(0, pool.getNumActive());
        assertTrue(pool.getNumIdle() > 0);
    }

    @Test
    @DisplayName("반납된 커넥션을 재사용할 수 있다")
    void reuseReturnedConnection() throws Exception {
        // given
        TcpClientConnection connection1 = pool.borrowConnection();
        connection1.write("first message");
        connection1.read(1024);
        pool.returnConnection(connection1);

        // when
        TcpClientConnection connection2 = pool.borrowConnection();
        connection2.write("second message");
        byte[] response = connection2.read(1024);

        // then
        assertEquals("SECOND MESSAGE", new String(response, "UTF-8"));
        assertTrue(connection2.isValid());

        // cleanup
        pool.returnConnection(connection2);
    }

    @Test
    @DisplayName("검증에 실패한 커넥션은 자동으로 제거되고 새 커넥션이 생성된다")
    void invalidConnectionIsReplacedAutomatically() throws Exception {
        // given
        TcpClientConnection connection = pool.borrowConnection();
        connection.close(); // 커넥션을 강제로 닫음
        pool.returnConnection(connection);

        // when - 다음 borrow 시 검증 실패한 커넥션 대신 새 커넥션 제공
        TcpClientConnection newConnection = pool.borrowConnection();

        // then
        assertNotNull(newConnection);
        assertTrue(newConnection.isValid());

        // cleanup
        pool.returnConnection(newConnection);
    }
}

