 package com.online;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for user login.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        String un = "root";
        String pw = "admin";
        String url = "jdbc:mysql://localhost:3306/online_voting";

        try{
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection con = DriverManager.getConnection(url, un, pw);            
            String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                request.getSession().setAttribute("userId", userId); // Store user ID in session
                response.sendRedirect("vote.html"); // Redirect to vote page
                
            } else {
                response.getWriter().write("Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Login failed: " + e.getMessage());
        }
    }
}