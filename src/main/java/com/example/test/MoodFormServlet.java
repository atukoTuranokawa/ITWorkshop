package com.example.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MoodFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
        response.getWriter().println("<link rel='stylesheet' type='text/css' href='styles/style.css'>");
        response.getWriter().println("</head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>気分記録フォーム</h1>");
        response.getWriter().println("<form method='POST' action='MoodFormServlet' class='form'>");
        response.getWriter().println("<label for='date'>日付:</label>");
        response.getWriter().println("<input type='date' id='date' name='date'><br>");
        response.getWriter().println("<label for='mood'>気分:</label>");
        response.getWriter().println("<select id='mood' name='mood'>");
        response.getWriter().println("<option value='Good'>Good</option>");
        response.getWriter().println("<option value='Neutral'>Neutral</option>");
        response.getWriter().println("<option value='Bad'>Bad</option>");
        response.getWriter().println("</select><br>");
        response.getWriter().println("<label for='notes'>メモ:</label>");
        response.getWriter().println("<textarea id='notes' name='notes'></textarea><br>");
        response.getWriter().println("<input type='submit' value='記録する' class='btn'>");
        response.getWriter().println("</form>");
        response.getWriter().println("<a href='MoodListServlet' class='btn'>戻る</a>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // フォームからのデータを取得
        String date = request.getParameter("date");
        String mood = request.getParameter("mood");
        String notes = request.getParameter("notes");

        try {
            // データベース接続
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "");

            // INSERTクエリ
            String insertSQL = "INSERT INTO moods (USER_ID, DATE, MOOD, NOTES) VALUES (1, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, date);
            pstmt.setString(2, mood);
            pstmt.setString(3, notes);
            pstmt.executeUpdate();

            conn.close();

            // 気分記録一覧へリダイレクト
            response.sendRedirect("MoodListServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<html><body><p>Error: " + e.getMessage() + "</p></body></html>");
        }
    }
}
