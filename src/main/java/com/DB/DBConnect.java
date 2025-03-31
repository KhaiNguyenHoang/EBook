/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nhat_Anh
 */
public class DBConnect {

    private static Connection conn;
    private static String user = "sa";
    private static String pass = "123456";
    private static String url = "jdbc:sqlserver://localhost:1433;databaseName=EbookApp;encrypt=true;trustServerCertificate=true";

    public static Connection getConn() {
        if (conn == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(url, user, pass);
//                DatabaseInitializer databaseInitializer = new DatabaseInitializer();
//                databaseInitializer.initializeDatabase();

            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conn;
    }

    /**
     * Hashes a password using MD5 algorithm
     *
     * @param password - The plain text password to hash
     * @return A string representing the MD5 hashed password
     */
    public static String hashPasswordMD5(String password) {
        String hashedPassword = null;
        if (password != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                byte[] digest = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                hashedPassword = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return hashedPassword;
    }

    public static class DatabaseInitializer {

        public DatabaseInitializer() {
        }

        private static final String DATABASE_NAME = "EbookApp";

        public void initializeDatabase() {
            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                Statement stmt = conn.createStatement();
                // 1?? T?o Database n?u ch?a có
                String createDB = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = '" + DATABASE_NAME + "') "
                        + "BEGIN CREATE DATABASE " + DATABASE_NAME + " END";
                stmt.executeUpdate(createDB);
                System.out.println("? Database ki?m tra xong!");

                // 2?? Chuy?n sang database EbookApp
                stmt.execute("USE " + DATABASE_NAME);

                // 3?? T?o b?ng User
                String createUserTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='User' AND xtype='U') "
                        + "BEGIN "
                        + "CREATE TABLE User ("
                        + "userID INT IDENTITY(1,1) PRIMARY KEY, "
                        + "Name NVARCHAR(255) NOT NULL, "
                        + "Email NVARCHAR(255) UNIQUE NOT NULL, "
                        + "Password NVARCHAR(255) NOT NULL, "
                        + "Phno NVARCHAR(20), "
                        + "Address NVARCHAR(500), "
                        + "Landmark NVARCHAR(255), "
                        + "City NVARCHAR(100), "
                        + "State NVARCHAR(100), "
                        + "Pincode NVARCHAR(20) "
                        + ") END";
                stmt.executeUpdate(createUserTable);
                System.out.println("? B?ng User ki?m tra xong!");

                // 4?? T?o b?ng BookDtls
                String createBookTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='BookDtls' AND xtype='U') "
                        + "BEGIN "
                        + "CREATE TABLE BookDtls ("
                        + "bookID INT IDENTITY(1,1) PRIMARY KEY, "
                        + "bookName NVARCHAR(255) NOT NULL, "
                        + "author NVARCHAR(255) NOT NULL, "
                        + "price NVARCHAR(50), "
                        + "bookCategory NVARCHAR(100), "
                        + "status NVARCHAR(50), "
                        + "photo NVARCHAR(255), "
                        + "userEmail NVARCHAR(255) FOREIGN KEY REFERENCES User(Email) ON DELETE CASCADE"
                        + ") END";
                stmt.executeUpdate(createBookTable);
                System.out.println("? B?ng BookDtls ki?m tra xong!");

                // 5?? T?o b?ng BookOrder
                String createBookOrderTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='BookOrder' AND xtype='U') "
                        + "BEGIN "
                        + "CREATE TABLE BookOrder ("
                        + "id INT IDENTITY(1,1) PRIMARY KEY, "
                        + "orderId NVARCHAR(50), "
                        + "userName NVARCHAR(255), "
                        + "email NVARCHAR(255) FOREIGN KEY REFERENCES User(Email) ON DELETE CASCADE, "
                        + "phno NVARCHAR(20), "
                        + "fulladd NVARCHAR(500), "
                        + "paymentType NVARCHAR(50), "
                        + "bookName NVARCHAR(255), "
                        + "author NVARCHAR(255), "
                        + "price NVARCHAR(50), "
                        + "cartId INT "
                        + ") END";
                stmt.executeUpdate(createBookOrderTable);
                System.out.println("? B?ng BookOrder ki?m tra xong!");

                // 6?? T?o b?ng GoogleUser
                String createGoogleUserTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='GoogleUser' AND xtype='U') "
                        + "BEGIN "
                        + "CREATE TABLE GoogleUser ("
                        + "googleID NVARCHAR(255) PRIMARY KEY, "
                        + "userID INT FOREIGN KEY REFERENCES User(userID) ON DELETE CASCADE, "
                        + "authToken NVARCHAR(500), "
                        + "GGEmail NVARCHAR(255) UNIQUE, "
                        + "GGName NVARCHAR(255), "
                        + "password NVARCHAR(255) "
                        + ") END";
                stmt.executeUpdate(createGoogleUserTable);
                System.out.println("? B?ng GoogleUser ki?m tra xong!");

                // 7?? T?o b?ng GitHubUser
                String createGitHubUserTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='GitHubUser' AND xtype='U') "
                        + "BEGIN "
                        + "CREATE TABLE GitHubUser ("
                        + "gitHubID NVARCHAR(255) PRIMARY KEY, "
                        + "userID INT FOREIGN KEY REFERENCES User(userID) ON DELETE CASCADE, "
                        + "authToken NVARCHAR(500), "
                        + "GHEmail NVARCHAR(255) UNIQUE, "
                        + "GHName NVARCHAR(255), "
                        + "password NVARCHAR(255) "
                        + ") END";
                stmt.executeUpdate(createGitHubUserTable);
                System.out.println("? B?ng GitHubUser ki?m tra xong!");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
