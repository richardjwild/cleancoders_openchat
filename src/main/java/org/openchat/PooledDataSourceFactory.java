package org.openchat;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.util.Properties;

public class PooledDataSourceFactory {

    private static final int MAX_CONNECTIONS = 1;
    private static final KeyedObjectPoolFactory STMT_POOL_FACTORY = null;
    private static final String VALIDATION_QUERY = null;
    private static final boolean DEFAULT_READ_ONLY = false;
    private static final boolean DEFAULT_AUTO_COMMIT = true;

    private final String url;
    private final String username;
    private final String password;

    public PooledDataSourceFactory(Properties properties) {
        url = properties.getProperty("db.url");
        username = properties.getProperty("db.username");
        password = properties.getProperty("db.password");
    }

    public DataSource getDataSource() {
        GenericObjectPool objectPool = new GenericObjectPool();
        objectPool.setMaxActive(MAX_CONNECTIONS);
        new PoolableConnectionFactory(
                new DriverManagerConnectionFactory(url, username, password),
                objectPool,
                STMT_POOL_FACTORY,
                VALIDATION_QUERY,
                DEFAULT_READ_ONLY,
                DEFAULT_AUTO_COMMIT);
        return new PoolingDataSource(objectPool);
    }
}
