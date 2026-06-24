package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
   
    private static final String URL = "jdbc:mysql://localhost:3306/ghads_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 
    
    private static Connection connection = null;

   

    public static Connection getConnection() {
        if (connection == null) {
            try {
              
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ تم الاتصال بقاعدة بيانات GHADS بنجاح!");
            } catch (ClassNotFoundException e) {
                System.out.println("❌ خطأ: لم يتم العثور على MySQL Driver!");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("❌ خطأ: فشل الاتصال بقاعدة البيانات!");
                e.printStackTrace();
            }
        }
        return connection;
    }
}