package com.binghe.springtransactionalandaop.service;

import com.binghe.springtransactionalandaop.domain.Money;

public interface TransferService {

    void transfer(Long fromCustomerId, Long toCustomerId, Money amount);
}
