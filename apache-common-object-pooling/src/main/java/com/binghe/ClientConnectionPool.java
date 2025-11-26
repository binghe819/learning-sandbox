package com.binghe;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.Closeable;

public class ClientConnectionPool implements Closeable {
    private final GenericObjectPool<TcpClientConnection> pool;

    public ClientConnectionPool(GenericObjectPool<TcpClientConnection> pool) {
        this.pool = pool;
    }

    /**
     * 풀에서 커넥션을 빌려옵니다.
     * 사용 가능한 커넥션이 없으면 대기하거나 새로 생성합니다.
     *
     * @return TcpClientConnection 객체
     * @throws Exception 커넥션을 빌려오는데 실패한 경우
     */
    public TcpClientConnection borrowConnection() throws Exception {
        return pool.borrowObject();
    }

    /**
     * 사용이 끝난 커넥션을 풀에 반납합니다.
     *
     * @param connection 반납할 커넥션
     */
    public void returnConnection(TcpClientConnection connection) {
        if (connection != null) {
            pool.returnObject(connection);
        }
    }

    /**
     * 잘못된 커넥션을 무효화하고 풀에서 제거합니다.
     * 커넥션에 문제가 발생했을 때 사용합니다.
     *
     * @param connection 무효화할 커넥션
     * @throws Exception 무효화 중 예외 발생 시
     */
    public void invalidateConnection(TcpClientConnection connection) throws Exception {
        if (connection != null) {
            pool.invalidateObject(connection);
        }
    }

    /**
     * 커넥션을 빌려서 작업을 수행하고 자동으로 반납/무효화를 처리합니다.
     * try-catch-finally 패턴을 캡슐화한 편의 메서드입니다.
     *
     * 사용 예시:
     * <pre>
     * pool.executeWithConnection(connection -> {
     *     connection.write("Hello");
     *     byte[] response = connection.read(1024);
     *     return new String(response);
     * });
     * </pre>
     *
     * @param action 커넥션을 사용하는 작업
     * @param <T> 작업의 반환 타입
     * @return 작업 수행 결과
     * @throws Exception 커넥션 빌리기 또는 작업 수행 중 예외 발생 시
     */
    public <T> T executeWithConnection(ConnectionAction<T> action) throws Exception {
        TcpClientConnection connection = null;
        try {
            connection = borrowConnection();
            T result = action.execute(connection);
            returnConnection(connection);
            return result;
        } catch (Exception e) {
            if (connection != null) {
                invalidateConnection(connection);
            }
            throw e;
        }
    }

    /**
     * 커넥션을 사용하는 작업을 정의하는 함수형 인터페이스
     *
     * @param <T> 작업의 반환 타입
     */
    @FunctionalInterface
    public interface ConnectionAction<T> {
        T execute(TcpClientConnection connection) throws Exception;
    }

    /**
     * 현재 활성 상태(빌려진 상태)의 커넥션 개수를 반환합니다.
     *
     * @return 활성 커넥션 수
     */
    public int getNumActive() {
        return pool.getNumActive();
    }

    /**
     * 현재 유휴 상태(풀에 대기 중)인 커넥션 개수를 반환합니다.
     *
     * @return 유휴 커넥션 수
     */
    public int getNumIdle() {
        return pool.getNumIdle();
    }

    /**
     * 풀의 모든 유휴 커넥션을 제거합니다.
     */
    public void clear() {
        pool.clear();
    }

    /**
     * 풀이 닫혀있는지 확인합니다.
     *
     * @return 풀이 닫혀있으면 true, 아니면 false
     */
    public boolean isClosed() {
        return pool.isClosed();
    }

    /**
     * 커넥션 풀을 종료하고 모든 리소스를 정리합니다.
     * 모든 활성 커넥션이 반납될 때까지 기다립니다.
     */
    @Override
    public void close() {
        pool.close();
    }

    /**
     * 커넥션 풀의 상태 정보를 문자열로 반환합니다.
     *
     * @return 풀 상태 정보
     */
    public String getPoolStats() {
        return String.format(
                "ConnectionPool[Active=%d, Idle=%d, Total=%d, Closed=%s]",
                getNumActive(),
                getNumIdle(),
                getNumActive() + getNumIdle(),
                isClosed()
        );
    }
}
