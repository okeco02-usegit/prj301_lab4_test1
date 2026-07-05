<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    String action = (String) request.getAttribute("action");
    String message = (String) request.getAttribute("message");
    String pageLink = (String) request.getAttribute("page");
    if (action == null) action = "Message";
    if (message == null) message = "";
    if (pageLink == null || pageLink.trim().isEmpty()) pageLink = "Login.html";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Display Message</title>
</head>
<body>
    <h2><%= h(action) %></h2>
    <p><%= h(message) %></p>
    <p><a href="<%= h(pageLink) %>">Back</a></p>
</body>
</html>
