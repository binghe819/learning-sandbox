//package com.binghe
//
//import io.r2dbc.pool.ConnectionPool
//import io.r2dbc.pool.ConnectionPoolConfiguration
//import io.r2dbc.spi.ConnectionFactory
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
//
//@EnableR2dbcRepositories
//@Configuration
//class R2dbcConfiguration : AbstractR2dbcConfiguration() {
//
//    @Bean
//    override fun connectionFactory(): ConnectionFactory {
//        val mySQLConnectionFactory = MySQLConnectionFactory(mySQLConnectionConfig())
//        val jasyncConnectionFactory = JasyncConnectionFactory(mySQLConnectionFactory)
//        val r2dbcPoolConfig = connectionPoolConfiguration(jasyncConnectionFactory)
//        val r2dbcPool = ConnectionPool(r2dbcPoolConfig)
//
//        // Application 시작 전에 DB Connection Pool 초기화
//        r2dbcPool.warmup().block()
//
//        return r2dbcPool
//    }
//
//    private fun connectionPoolConfiguration(connectionFactory: ConnectionFactory) =
//        ConnectionPoolConfiguration.builder()
//            .connectionFactory(connectionFactory)
//            .initialSize(25)
//            .minIdle(25)
//            .maxSize(25)
//            .build()
//
//    private fun mySQLConnectionConfig() =
//        MySqlConnectionConfiguration(
//            host = "db",
//            username = "root",
//            password = "root",
//            port = 3306,
//            database = "sample",
//            connectionTimeout = 2_000,
//        )
//}