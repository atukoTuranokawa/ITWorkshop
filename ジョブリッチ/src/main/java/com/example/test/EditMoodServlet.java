package com.example.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EditMoodServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int moodId = Integer.parseInt(request.getParameter("moodId"));

        try {
            // JDBCドライバーをロード
            Class.forName("org.h2.Driver");

            // データベース接続
            Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "");

            // 編集対象データを取得
            String query = "SELECT * FROM moods WHERE MOOD_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, moodId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 編集フォームを出力
                out.println("<html><body>");
                out.println("<h1>気分記録の編集</h1>");
                out.println("<form method='post' action='EditMoodServlet'>");
                out.println("<input type='hidden' name='moodId' value='" + moodId + "'>");
                out.println("日付: <input type='date' name='date' value='" + rs.getDate("DATE") + "'><br>");
                out.println("気分: <select name='mood'>");
                out.println("<option value='Good'" + (rs.getString("MOOD").equals("Good") ? " selected" : "") + ">Good</option>");
                out.println("<option value='Neutral'" + (rs.getString("MOOD").equals("Neutral") ? " selected" : "") + ">Neutral</option>");
                out.println("<option value='Bad'" + (rs.getString("MOOD").equals("Bad") ? " selected" : "") + ">Bad</option>");
                out.println("</select><br>");
                out.println("メモ: <textarea name='notes'>" + rs.getString("NOTES") + "</textarea><br>");
                out.println("<input type='submit' value='保存'>");
                out.println("</form>");
                out.println("</body></html>");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int moodId = Integer.parseInt(request.getParameter("moodId"));
        String date = request.getParameter("date");
        String mood = request.getParameter("mood");
        String notes = request.getParameter("notes");

        try {
            // JDBCドライバーをロード
            Class.forName("org.h2.Driver");

            // データベース接続
            Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "");

            // データを更新
            String updateSQL = "UPDATE moods SET DATE = ?, MOOD = ?, NOTES = ? WHERE MOOD_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setString(1, date);
            pstmt.setString(2, mood);
            pstmt.setString(3, notes);
            pstmt.setInt(4, moodId);
            pstmt.executeUpdate();

            conn.close();

            // リストにリダイレクト
            response.sendRedirect("MoodListServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
