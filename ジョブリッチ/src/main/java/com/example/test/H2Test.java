package com.example.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class H2Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // JDBC URL
        String url = "jdbc:h2:~/mood_tracker";
        String user = "sa"; // デフォルトユーザー
        String password = ""; // デフォルトパスワード（空）

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // H2ドライバをロード
            Class.forName("org.h2.Driver");

            // データベース接続
            Connection conn = DriverManager.getConnection(url, user, password);

            // SQLステートメント
            Statement stmt = conn.createStatement();

            // データ取得（データ挿入は省略）
            String selectSQL = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(selectSQL);

            // HTMLでデータを表示
            out.println("<html><body>");
            out.println("<h1>Database Results:</h1>");
            while (rs.next()) {
                out.println("<p>ID: " + rs.getInt("USER_ID") + "</p>");
                out.println("<p>Name: " + rs.getString("NAME") + "</p>");
                out.println("<p>Username: " + rs.getString("USERNAME") + "</p>");
                out.println("<p>Email: " + rs.getString("EMAIL") + "</p>");
                out.println("<p>Password: " + rs.getString("PASSWORD") + "</p>");
                out.println("<hr>");
            }
            out.println("</body></html>");

            // 接続を閉じる
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
