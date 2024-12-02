package com.online;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation for displaying voting results.
 */
@WebServlet("/results")
public class ResultServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ResultServlet is called.");  // Debugging

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/online_voting", "root", "admin")) {

            // Query to fetch candidate names and vote counts
            String sql = "SELECT c.name, COUNT(v.candidate_id) as vote_count " +
                         "FROM votes v " +
                         "JOIN candidates c ON v.candidate_id = c.candidate_id " +
                         "GROUP BY v.candidate_id";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Flag to check if there are any results
            boolean hasResults = false;

            // HTML response structure
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head><meta charset='UTF-8'><title>Results</title></head>");
            out.println("<body>");
            out.println("<h1>Voting Results</h1>");
            out.println("<table border='1'><tr><th>Candidate</th><th>Votes</th></tr>");

            // Iterate through results and display candidate names and vote counts
            while (rs.next()) {
                hasResults = true;
                String candidateName = rs.getString("name");
                int voteCount = rs.getInt("vote_count");
                out.println("<tr><td>" + candidateName + "</td><td>" + voteCount + "</td></tr>");
            }

            // If no results, show a message
            if (!hasResults) {
                out.println("<tr><td colspan='2'>No results found</td></tr>");
            }

            out.println("</table>");
            out.println("<p><a href='index.html'>Back to Home</a></p>");
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error retrieving results: " + e.getMessage() + "</p>");
        }
    }
}
  