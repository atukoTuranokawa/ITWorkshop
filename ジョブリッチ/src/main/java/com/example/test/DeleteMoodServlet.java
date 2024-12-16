package com.example.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteMoodServlet extends HttpServlet {
    // POSTリクエストの処理（削除機能）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("POSTリクエストが到達しました");

        // フォームから送られた moodId を取得
        String moodIdStr = request.getParameter("moodId");
        System.out.println("受け取ったmoodId: " + moodIdStr);

        // moodIdがnullまたは空の場合のエラーチェック
        if (moodIdStr == null || moodIdStr.isEmpty()) {
            response.getWriter().println("<p>Error: 無効なIDが指定されました。</p>");
            return;
        }

        try {
            int moodId = Integer.parseInt(moodIdStr);

            // H2データベースへの接続
            try (Connection conn = DriverManager.getConnection("jdbc:h2:~/mood_tracker", "sa", "")) {
                // 削除SQL文の実行
                String deleteSQL = "DELETE FROM moods WHERE MOOD_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                    pstmt.setInt(1, moodId);
                    int affectedRows = pstmt.executeUpdate();

                    // 削除成功時のリダイレクト
                    if (affectedRows > 0) {
                        response.sendRedirect("MoodListServlet");
                    } else {
                        response.getWriter().println("<p>Error: 指定されたIDのデータが存在しません。</p>");
                    }
                }
            }
        } catch (NumberFormatException e) {
            // IDが整数でない場合のエラー処理
            response.getWriter().println("<p>Error: IDは整数で指定してください。</p>");
        } catch (Exception e) {
            // データベース接続やクエリ実行時のエラー処理
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
        }
    }

    // GETリクエストの処理（エラーメッセージ表示）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<p>このサーブレットはPOSTリクエスト専用です。</p>");
        response.getWriter().println("<p>削除するには正しいフォームから操作してください。</p>");
    }
}
