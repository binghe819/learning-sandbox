package com.binghe.one_before_transaction_manager.service;

import com.binghe.one_before_transaction_manager.dao.ConnectionFactory;
import com.binghe.one_before_transaction_manager.dao.UserAccountDaoV2;
import com.binghe.domain.UserAccount;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션은 비즈니스 로직이 있는 서비스 계층에서 시작해야 한다. 비즈니스 로직이 잘못되면 해당 비즈니스 로직으로 인해 문제가 되는 부분을 함께 롤백해야 하기 때문이다.
 * 그런데 트랜잭션을 시작하려면 커넥션이 필요하다. 결국 서비스 계층에서 커넥션을 만들고, 트랜잭션 커밋 이후에 커넥션을 종료해야 한다.
 * 애플리케이션에서 DB 트랜잭션을 사용하려면 트랜잭션을 사용하는 동안 같은 커넥션을 유지해야한다. 그래야 같은 세션을 사용할 수 있다.
 * 애플리케이션에서 같은 커넥션을 유지하는 가장 단순한 방법은 커넥션을 파라미터로 전달해서 같은 커넥션이 사용되도록 유지하는 것이다.
 */
public class UserAccountServiceV2 {

    private final ConnectionFactory connectionFactory;
    private final UserAccountDaoV2 userAccountDaoV2;

    public UserAccountServiceV2(ConnectionFactory connectionFactory, UserAccountDaoV2 userAccountDaoV2) {
        this.connectionFactory = connectionFactory;
        this.userAccountDaoV2 = userAccountDaoV2;
    }

    public void transfer(Long from, Long to, Long amount) throws SQLException {
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool()) {
            conn.setAutoCommit(false);
            try {
                UserAccount fromAccount = userAccountDaoV2.findByUserId(conn, from);

                if (fromAccount.getBalance() < amount) {
                    throw new IllegalStateException("잔액이 부족합니다. 현재 잔액: " + fromAccount.getBalance());
                }

                userAccountDaoV2.update(conn, new UserAccount(from, fromAccount.getBalance() - amount));

                UserAccount toAccount = userAccountDaoV2.findByUserId(conn, to);
                userAccountDaoV2.update(conn, new UserAccount(to, toAccount.getBalance() + amount));

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
