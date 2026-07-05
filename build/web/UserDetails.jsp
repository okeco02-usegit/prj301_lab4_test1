<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Models.DTO.User" %>
<%!
    private String h(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>
<%
    User user = (User) request.getAttribute("UserDetails");
    User loggedIn = (User) session.getAttribute("UserLoggedIn");
    if (loggedIn == null || user == null) {
        response.sendRedirect("Login.html");
        return;
    }
    boolean admin = loggedIn.isAdmin();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Details</title>
</head>
<body>
    <h2>User Details</h2>

    <% if (admin) { %>
        <form action="UserController" method="POST">
            <input type="hidden" name="action" value="Update">
            <p>
                UserName
                <input type="text" name="txtUserName" value="<%= h(user.getUserName()) %>" readonly>
            </p>
            <p>
                Password
                <input type="text" name="txtPassword" value="<%= h(user.getPassword()) %>" required>
            </p>
            <p>
                LastName
                <input type="text" name="txtLastName" value="<%= h(user.getLastName()) %>" required>
            </p>
            <p>
                Is Admin
                <input type="checkbox" name="chkIsAdmin" value="true" <%= user.isAdmin() ? "checked" : "" %>>
            </p>
            <p>
                <input type="submit" value="Update">
                <input type="reset" value="Reset">
            </p>
        </form>
        <p><a href="UserController?action=Search">Back to Search</a></p>
    <% } else { %>
        <p>UserName: <%= h(user.getUserName()) %></p>
        <p>Password: <%= h(user.getPassword()) %></p>
        <p>LastName: <%= h(user.getLastName()) %></p>
        <p>Role: <%= user.isAdmin() ? "Admin" : "User" %></p>
        <p><a href="Login.html">Back</a></p>
    <% } %>
</body>
</html>
