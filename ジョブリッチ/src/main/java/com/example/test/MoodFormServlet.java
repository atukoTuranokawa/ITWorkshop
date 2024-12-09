package com.example.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MoodFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // フォームのHTMLを出力
        out.println("<html><body>");
        out.println("<h1>気分記録フォーム</h1>");
        out.println("<form method='post' action='MoodFormServlet'>");
        out.println("日付: <input type='date' name='date'><br>");
        out.println("気分: <select name='mood'>");
        out.println("<option value='Good'>Good</option>");
        out.println("<option value='Neutral'>Neutral</option>");
        out.println("<option value='Bad'>Bad</option>");
        out.println("</select><br>");
        out.println("メモ: <textarea name='notes'></textarea><br>");
        out.println("<input type='submit' value='記録する'>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // フォームからデータを取得
        String date = request.getParameter("date");
        String mood = request.getParameter("mood");
        String notes = request.getParameter("notes");

        try {
            // JDBCドライバーをロード
            Class.forName("org.h2.Driver");

            // データベース接続
            Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "");

            // データを挿入
            String insertSQL = "INSERT INTO moods (USER_ID, DATE, MOOD, NOTES) VALUES (1, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, date);
            pstmt.setString(2, mood);
            pstmt.setString(3, notes);
            pstmt.executeUpdate();

            // 接続を閉じる
            conn.close();

            // 成功メッセージ
            response.sendRedirect("MoodListServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
