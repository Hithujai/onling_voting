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

/**
 * Servlet implementation for user voting.
 */
@WebServlet("/vote")
public class VoteServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get the candidate ID and user ID from the request/session
        int candidateId = Integer.parseInt(request.getParameter("candidate_id")); // Ensure the form sends candidate_id
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            response.getWriter().write("Session expired. Please log in again.");
            return;
        }

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/online_voting", "root", "admin")) { // Database credentials
            
            // Insert the vote into the database
            String sql = "INSERT INTO votes (user_id, candidate_id) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, candidateId);
            ps.executeUpdate();

            // Redirect to results page
            response.sendRedirect("results.html");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Voting failed: " + e.getMessage());
        }
    }
}
