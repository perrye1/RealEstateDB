package edu.nku.csc450.realEstate.web.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MysqlContextListener implements ServletContextListener {

    //we were experiencing a persistant issue with database connectivity dropping after about an hour of idle time, when using the previous method of one static connection.
    //this current method implements a true JDBC pool, which can contain many separate connections, and hand them out to the application on demand. It is also responsible for
    //cleaning up idle and abandoned connections, and verifying a connection's integrity before handing it to the app. 

    //we create a datamember for the PoolProperties object, that defines the setting of the pool.
    public static PoolProperties pool;
    //we create a datamember for the dataSource, which is the actual pool
    public static DataSource data_source;

    //when tomcat comes online, we generate our properties object, giving it the username, password, and connection string for the DB
    @Override
    public void contextInitialized(ServletContextEvent sce) {
            
            pool = new PoolProperties();
            
            pool.setUrl("jdbc:mysql://chatdb.ceqzmsawg0cp.us-west-2.rds.amazonaws.com/real_estate");
            pool.setDriverClassName("com.mysql.jdbc.Driver");
            pool.setUsername("chat");
            pool.setPassword("nkuchatapp");
            pool.setJmxEnabled(true);
            pool.setTestWhileIdle(true);
            pool.setTestOnBorrow(true);
            pool.setValidationQuery("SELECT 1");
            pool.setTestOnReturn(true);
            pool.setValidationInterval(30000);
            pool.setTimeBetweenEvictionRunsMillis(30000);
            pool.setMaxActive(1000);
            pool.setInitialSize(50);
            pool.setMaxWait(10000);
            pool.setRemoveAbandonedTimeout(60);
            pool.setMinEvictableIdleTimeMillis(30000);
            pool.setMinIdle(10);
            pool.setLogAbandoned(true);
            pool.setRemoveAbandoned(true);
            pool.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;"+"org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer");
            
            data_source = new DataSource();

            //we configure the DataSource object according the properties in the poolProperties object
            data_source.setPoolProperties(pool);
    }

    //returns a static instance of the DataSource object to the caller. The DataSource is passed to the repositories, where connections will be opened from it. 
    public static DataSource getDataSource(){
        return data_source;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //there is no need to close anything here, because the DataSource object is never left open. It is only "opened" when a method requests a new connection from it.
        //in this application, whenever a method opens a connetion off of the dataSource (using tryWithResources), the connection is closed when the try block completes. 
    }
}