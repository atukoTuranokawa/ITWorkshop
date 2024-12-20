package com.example.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SupportMoodListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.username, m.date, m.mood, m.notes " +
                     "FROM moods m " +
                     "JOIN users u ON m.user_id = u.user_id " +
                     "WHERE u.is_shared = true " +
                     "ORDER BY m.date DESC")) {
            
            ResultSet rs = stmt.executeQuery();
            
            out.println("<html><body>");
            out.println("<h1>共有された勤務状況</h1>");
            out.println("<table border='1'>");
            out.println("<tr><th>ユーザー名</th><th>日付</th><th>気分</th><th>メモ</th></tr>");
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getDate("date") + "</td>");
                out.println("<td>" + rs.getString("mood") + "</td>");
                out.println("<td>" + rs.getString("notes") + "</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
