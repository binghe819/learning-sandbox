package com.binghe.service;

import com.binghe.dao.UserAccountDaoV5;
import com.binghe.dao.UserDaoV5;
import com.binghe.domain.UserAccount;
import org.springframework.transaction.annotation.Transactional;

public class UserAccountServiceV5 {

    private final UserDaoV5 userDaoV5;
    private final UserAccountDaoV5 userAccountDaoV5;

    public UserAccountServiceV5(UserDaoV5 userDaoV5, UserAccountDaoV5 userAccountDaoV5) {
        this.userDaoV5 = userDaoV5;
        this.userAccountDaoV5 = userAccountDaoV5;
    }

    @Transactional
    public void accountTransfer(Long fromId, Long toId, long money) {
        UserAccount fromAccount = userAccountDaoV5.findByUserId(fromId);
        if (fromAccount.getBalance() < money) {
            throw new IllegalStateException("잔액이 부족합니다. 현재 잔액: " + fromAccount.getBalance());
        }

        userAccountDaoV5.update(new UserAccount(fromId, fromAccount.getBalance() - money));

        UserAccount toAccount = userAccountDaoV5.findByUserId(toId);
        userAccountDaoV5.update(new UserAccount(toId, toAccount.getBalance() + money));
    }
}
