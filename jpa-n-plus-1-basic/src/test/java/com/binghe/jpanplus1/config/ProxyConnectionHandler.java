package com.binghe.jpanplus1.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProxyConnectionHandler implements InvocationHandler {

    private final Connection connection;
    private final QueryCounter queryCounter;

    public ProxyConnectionHandler(Connection connection, QueryCounter queryCounter) {
        this.connection = connection;
        this.queryCounter = queryCounter;
    }

    // 쿼리 카운팅 (부가 기능 구현)
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (queryCounter.isCountable()) {
            if (method.getName().equals("prepareStatement")) {
                return getConnectionWithCountQuery(method, args); // 핵심 로직 호출 및 반환
            }
        }
        return method.invoke(connection, args); // 핵심 로직 호출 및 반환
    }

    // 카운트
    private Object getConnectionWithCountQuery(Method method, Object[] args)
        throws InvocationTargetException, IllegalAccessException {
        PreparedStatement preparedStatement = (PreparedStatement) method.invoke(connection, args);

        for (Object statement : args) {
            if (isQueryStatement(statement)) {
                queryCounter.countOne();
                break;
            }
        }
        return preparedStatement;
    }

    // preparedStatement가 호출될 때 해당 매개변수가 String 형식이며, select으로 시작하는 쿼리인지 체크.
    private boolean isQueryStatement(Object statement) {
        if (statement.getClass().isAssignableFrom(String.class)) {
            String sql = (String) statement;
            return sql.startsWith("select");
        }
        return false;
    }
}
