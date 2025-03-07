package com.binghe.integration;

//import com.binghe.config.TestDatabaseConfig;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class IntegrationTest {

    @Autowired
    private TestDatabaseConfig testDatabaseConfig;

    @AfterEach
    void tearDown() {
        testDatabaseConfig.clear();
    }
}
