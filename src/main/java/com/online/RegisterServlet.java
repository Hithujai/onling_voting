package com.online;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_voting", "root", "admin");
            PreparedStatement ps = con.prepareStatement("INSERT INTO users(username, password) VALUES(?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            response.sendRedirect("login.html");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Registration failed: " + e.getMessage());
        }
    }
}