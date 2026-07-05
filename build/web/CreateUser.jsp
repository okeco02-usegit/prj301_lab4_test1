<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create User</title>
</head>
<body>
    <h2>Create User</h2>

    <form action="UserController" method="POST">
        <input type="hidden" name="action" value="Create">
        <p>
            UserName <input type="text" name="txtUserName" required>
        </p>
        <p>
            Password <input type="password" name="txtPassword" required>
        </p>
        <p>
            LastName <input type="text" name="txtLastName" required>
        </p>
        <p>
            Is Admin <input type="checkbox" name="chkIsAdmin" value="true">
        </p>
        <p>
            <input type="submit" value="Create">
            <input type="reset" value="Reset">
        </p>
    </form>

    <p><a href="Login.html">Back to Login</a></p>
</body>
</html>
