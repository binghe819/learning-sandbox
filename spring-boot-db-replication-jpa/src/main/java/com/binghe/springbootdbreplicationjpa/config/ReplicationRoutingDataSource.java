package com.binghe.springbootdbreplicationjpa.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationRoutingDataSource.class);
    private static final String MASTER_NAME = "master";

    private List<String> slaveNames = new ArrayList<>();
    private int counter = 0;

    // Routing에 사용될 Datsource 주입하는 Setter
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        // Slave DataSource 가져올 때 사용할 Name 정의
        List<String> slaves = targetDataSources.keySet().stream()
            .map(Object::toString)
            .filter(string -> string.startsWith("slave"))
            .collect(Collectors.toList());

        slaveNames = slaves;
    }

    @Override
    protected String determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
            String slaveName = getNextSlaveName();

            LOGGER.info("Connected Slave DB Name : {}", slaveName);

            return slaveName;
        }

        LOGGER.info("Connected Master DB Name : {}", MASTER_NAME);
        return MASTER_NAME;
    }

    private String getNextSlaveName() {
        String nextSlave = slaveNames.get(counter);
        counter = (counter + 1) % slaveNames.size();
        return nextSlave;
    }
}
