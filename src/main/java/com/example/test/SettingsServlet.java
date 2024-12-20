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

public class SettingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "");
            String query = "SELECT is_shared FROM users WHERE user_id = 1";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            boolean isShared = false;
            if (rs.next()) {
                isShared = rs.getBoolean("is_shared");
            }

            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='styles/style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>共有設定</h1>");
            out.println("<form method='POST' action='SettingsServlet' class='form'>");
            out.println("<label for='is_shared'>勤務状況の共有:</label>");
            out.println("<input type='checkbox' id='is_shared' name='is_shared' " + (isShared ? "checked" : "") + ">");
            out.println("<br>");
            out.println("<input type='submit' value='保存' class='btn'>");
            out.println("</form>");
            out.println("<a href='MoodListServlet' class='btn'>戻る</a>");
            out.println("</body>");
            out.println("</html>");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isShared = request.getParameter("is_shared") != null;

        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "");
            String updateSQL = "UPDATE users SET is_shared = ? WHERE user_id = 1";
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setBoolean(1, isShared);
            pstmt.executeUpdate();

            conn.close();
            response.sendRedirect("SettingsServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
