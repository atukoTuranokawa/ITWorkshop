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

public class MoodListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "");
            Statement stmt = conn.createStatement();

            String query = "SELECT * FROM moods ORDER BY DATE DESC";
            ResultSet rs = stmt.executeQuery(query);

            // HTML開始部分
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='styles/style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>気分記録一覧</h1>");

            // 共有設定ページへのリンクを追加
            out.println("<a href='SettingsServlet' class='btn'>共有設定</a><br><br>");

            // テーブル作成
            out.println("<table>");
            out.println("<tr><th>ID</th><th>日付</th><th>気分</th><th>メモ</th><th>操作</th></tr>");

            while (rs.next()) {
                int moodId = rs.getInt("MOOD_ID");
                out.println("<tr>");
                out.println("<td>" + moodId + "</td>");
                out.println("<td>" + rs.getDate("DATE") + "</td>");
                out.println("<td>" + rs.getString("MOOD") + "</td>");
                out.println("<td>" + rs.getString("NOTES") + "</td>");
                out.println("<td>");
                out.println("<a href='EditMoodServlet?moodId=" + moodId + "'>編集</a> | ");
                out.println("<a href='DeleteMoodServlet?moodId=" + moodId + "' onclick='return confirm(\"本当に削除しますか？\");'>削除</a>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
