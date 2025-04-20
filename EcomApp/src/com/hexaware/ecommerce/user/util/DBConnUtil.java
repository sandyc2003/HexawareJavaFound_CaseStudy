/*
 * Author : Sandhya C
 * Date : 19/04/25
 * Description : Connection class to establish a connection with the MySQL database 
 *               for the e-commerce application.  
 *               
 */
package com.hexaware.ecommerce.user.util;

import java.sql.*;
import java.io.InputStream;
import java.util.Properties;

public class DBConnUtil {

    public static Connection getDBConnection() throws SQLException {
        Connection conn = null;
        Properties prop = new Properties();

        // Load from classpath (src folder)
        try (InputStream fis = DBConnUtil.class.getClassLoader().getResourceAsStream("Database.properties")) {

            if (fis == null) {
                throw new SQLException("Database.properties file not found in classpath");
            }

            prop.load(fis);

            String url = prop.getProperty("url");
            String user = prop.getProperty("username");
            String password = prop.getProperty("password");

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Failed to connect to the database", e);
        }

        return conn;
    }
}
