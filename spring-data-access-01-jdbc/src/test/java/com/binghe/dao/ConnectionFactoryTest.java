package com.binghe.dao;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {

    private static final Logger log = LoggerFactory.getLogger(ConnectionFactoryTest.class);

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    @Test
    void driverManager_매번_다른_커넥션을_반환한다() throws SQLException {
        Connection conn1 = connectionFactory.getNewConnection();
        Connection conn2 = connectionFactory.getNewConnection();
        log.info("connection={}, class={}", conn1, conn1.getClass());
        log.info("connection={}, class={}", conn2, conn2.getClass());

        assertNotSame(conn1, conn2);
    }

    @Test
    void connectionPool_동시에_꺼내면_다른_커넥션을_반환한다() throws SQLException {
        Connection conn1 = connectionFactory.getConnectionFromConnectionPool();
        Connection conn2 = connectionFactory.getConnectionFromConnectionPool();
        log.info("connection={}, class={}", conn1, conn1.getClass());
        log.info("connection={}, class={}", conn2, conn2.getClass());

        assertNotSame(conn1, conn2);
    }

    @Test
    void connectionPool_반납후_재사용하면_같은_커넥션을_반환한다() throws SQLException {
        Connection conn1 = connectionFactory.getConnectionFromConnectionPool();
        // HikariCP는 getConnection() 호출마다 새로운 HikariProxyConnection(래퍼)으로 감싸서 반환한다.
        // 따라서 프록시 객체(conn1 == conn2)는 항상 다르다.
        // unwrap()으로 실제 물리 커넥션(ConnectionImpl)을 꺼내야 동일 객체임을 확인할 수 있다.
        Connection physicalConn1 = conn1.unwrap(Connection.class);
        log.info("connection={}, class={}", conn1, conn1.getClass());
        conn1.close(); // 풀에 반납

        Connection conn2 = connectionFactory.getConnectionFromConnectionPool();
        Connection physicalConn2 = conn2.unwrap(Connection.class);
        log.info("connection={}, class={}", conn2, conn2.getClass());

        assertNotSame(conn1, conn2);               // 프록시 래퍼는 다른 객체
        assertSame(physicalConn1, physicalConn2);  // 실제 물리 커넥션은 같은 객체
    }
}
