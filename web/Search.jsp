<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>
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
    User loggedIn = (User) session.getAttribute("UserLoggedIn");
    if (loggedIn == null || !loggedIn.isAdmin()) {
        response.sendRedirect("Login.html");
        return;
    }

    String searchValue = (String) request.getAttribute("txtSearchValue");
    if (searchValue == null) searchValue = "";
    List<User> userList = (List<User>) request.getAttribute("SearchResult");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search User</title>
</head>
<body>
    <h2>Welcome to Web <%= h(loggedIn.getLastName()) %></h2>
    <h3>Search user by last name</h3>

    <form action="UserController" method="GET">
        <input type="hidden" name="action" value="Search">
        Enter search value:
        <input type="text" name="txtSearchValue" value="<%= h(searchValue) %>">
        <input type="submit" value="Search">
    </form>

    <p>
        <a href="CreateUser.jsp">Create new user</a> |
        <a href="Login.html">Logout</a>
    </p>

    <% if (userList != null) { %>
        <p>Number of users found: <%= userList.size() %></p>

        <% if (!userList.isEmpty()) { %>
            <table border="1" cellpadding="5" cellspacing="0">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>UserName</th>
                        <th>Password</th>
                        <th>LastName</th>
                        <th>Role</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int count = 1;
                        String encodedSearchValue = URLEncoder.encode(searchValue, "UTF-8");
                        for (User user : userList) {
                            String encodedUserName = URLEncoder.encode(user.getUserName(), "UTF-8");
                    %>
                    <tr>
                        <td><%= count++ %></td>
                        <td><%= h(user.getUserName()) %></td>
                        <td><%= h(user.getPassword()) %></td>
                        <td><%= h(user.getLastName()) %></td>
                        <td><%= user.isAdmin() ? "Admin" : "User" %></td>
                        <td>
                            <a href="UserController?action=Details&txtUserName=<%= encodedUserName %>">Update</a>
                            |
                            <a href="UserController?action=Delete&txtUserName=<%= encodedUserName %>&txtSearchValue=<%= encodedSearchValue %>"
                               onclick="return confirm('Are you sure you want to delete this user?');">Delete</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else { %>
            <p>No record found.</p>
        <% } %>
    <% } %>
</body>
</html>
