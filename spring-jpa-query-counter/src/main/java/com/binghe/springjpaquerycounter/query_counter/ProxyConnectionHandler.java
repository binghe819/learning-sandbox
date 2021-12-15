package com.binghe.springjpaquerycounter.query_counter;

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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (queryCounter.isCountable()) {
            if (method.getName().equals("prepareStatement") || method.getName().equals("createStatement")) {
                return getConnectionWithCountQuery(method, args);
            }
        }
        return method.invoke(connection, args);
    }

    private Object getConnectionWithCountQuery(Method method, Object[] args)
        throws InvocationTargetException, IllegalAccessException {
        PreparedStatement preparedStatement = (PreparedStatement) method.invoke(connection, args);

        for (Object statement : args) {
            if (isQueryStatement(statement)) {
                System.out.println("## Query : " + (String) statement);
                queryCounter.countOne();
                break;
            }
        }
        return preparedStatement;
    }

    private boolean isQueryStatement(Object statement) {
        if (statement.getClass().isAssignableFrom(String.class)) {
            String sql = (String) statement;
            return sql.startsWith("select");
        }
        return false;
    }
}
