package Controllers.User;

import Models.DAO.UserDAO;
import Models.DTO.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UpdateController", urlPatterns = {"/UpdateController"})
public class UpdateController extends HttpServlet {

    private static final String MESSAGE_PAGE = "DisplayMessage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String userName = request.getParameter("txtUserName");
        String password = request.getParameter("txtPassword");
        String lastName = request.getParameter("txtLastName");
        boolean isAdmin = request.getParameter("chkIsAdmin") != null;

        String message;

        try {
            HttpSession session = request.getSession(false);
            User userLoggedIn = session == null ? null : (User) session.getAttribute("UserLoggedIn");

            if (userLoggedIn == null || !userLoggedIn.isAdmin()) {
                message = "Please login by admin account.";
                request.setAttribute("page", "Login.html");
            } else if (isBlank(userName) || isBlank(password) || isBlank(lastName)) {
                message = "Please input full information.";
                request.setAttribute("page", "UserController?action=Details&txtUserName=" + safe(userName));
            } else {
                User user = new User(userName.trim(), password.trim(), lastName.trim(), isAdmin);
                UserDAO userDAO = new UserDAO();
                boolean result = userDAO.updateUser(user);
                message = result ? "User has been updated successfully." : "Update user failed or user not found.";
                request.setAttribute("page", "UserController?action=Search");
            }

            request.setAttribute("action", "Update User");
            request.setAttribute("message", message);
        } catch (Exception ex) {
            request.setAttribute("action", "Update User");
            request.setAttribute("page", "UserController?action=Search");
            request.setAttribute("message", "Error: " + ex.getMessage());
            log("UpdateController error: " + ex.getMessage(), ex);
        }

        RequestDispatcher rd = request.getRequestDispatcher(MESSAGE_PAGE);
        rd.forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
