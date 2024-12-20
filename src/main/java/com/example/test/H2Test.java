package com.example.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class H2Test extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "jdbc:h2:~/mood_tracker";
        String user = "sa";
        String password = "";

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            String selectSQL = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(selectSQL);

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

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
