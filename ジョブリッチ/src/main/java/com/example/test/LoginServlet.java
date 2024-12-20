package com.example.test;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
        response.getWriter().println("<link rel='stylesheet' type='text/css' href='styles/style.css'>");
        response.getWriter().println("</head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>ログインページ</h1>");
        response.getWriter().println("<form method='POST' action='LoginServlet' class='form'>");
        response.getWriter().println("<label for='username'>ユーザー名:</label>");
        response.getWriter().println("<input type='text' id='username' name='username'><br>");
        response.getWriter().println("<label for='password'>パスワード:</label>");
        response.getWriter().println("<input type='password' id='password' name='password'><br>");
        response.getWriter().println("<input type='submit' value='ログイン' class='btn'>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if ("testuser".equals(username) && "password123".equals(password)) {
            // 修正箇所：MoodFormServletにリダイレクト
            response.sendRedirect("MoodFormServlet");
        } else {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<html>");
            response.getWriter().println("<head>");
            response.getWriter().println("<link rel='stylesheet' type='text/css' href='styles/style.css'>");
            response.getWriter().println("</head>");
            response.getWriter().println("<body>");
            response.getWriter().println("<h1>ログイン結果</h1>");
            response.getWriter().println("<p class='error'>ログイン失敗！ユーザー名またはパスワードが間違っています。</p>");
            response.getWriter().println("<a href='LoginServlet' class='btn'>戻る</a>");
            response.getWriter().println("</body>");
            response.getWriter().println("</html>");
        }
    }
}
