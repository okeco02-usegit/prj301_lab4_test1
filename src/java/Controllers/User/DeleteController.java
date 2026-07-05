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
import java.net.URLEncoder;

@WebServlet(name = "DeleteController", urlPatterns = {"/DeleteController"})
public class DeleteController extends HttpServlet {

    private static final String MESSAGE_PAGE = "DisplayMessage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String userName = request.getParameter("txtUserName");
        String searchValue = request.getParameter("txtSearchValue");
        String message;

        try {
            HttpSession session = request.getSession(false);
            User userLoggedIn = session == null ? null : (User) session.getAttribute("UserLoggedIn");

            if (userLoggedIn == null || !userLoggedIn.isAdmin()) {
                message = "Please login by admin account.";
                request.setAttribute("page", "Login.html");
            } else if (userName == null || userName.trim().isEmpty()) {
                message = "UserName is empty.";
                request.setAttribute("page", "UserController?action=Search");
            } else if (userName.equals(userLoggedIn.getUserName())) {
                message = "You cannot delete the current logged in admin account.";
                request.setAttribute("page", "UserController?action=Search");
            } else {
                UserDAO userDAO = new UserDAO();
                boolean result = userDAO.deleteUser(userName);
                message = result ? "User has been deleted successfully." : "Delete user failed or user not found.";
                request.setAttribute("page", "UserController?action=Search&txtSearchValue=" + URLEncoder.encode(searchValue == null ? "" : searchValue, "UTF-8"));
            }

            request.setAttribute("action", "Delete User");
            request.setAttribute("message", message);
        } catch (Exception ex) {
            request.setAttribute("action", "Delete User");
            request.setAttribute("page", "UserController?action=Search");
            request.setAttribute("message", "Error: " + ex.getMessage());
            log("DeleteController error: " + ex.getMessage(), ex);
        }

        RequestDispatcher rd = request.getRequestDispatcher(MESSAGE_PAGE);
        rd.forward(request, response);
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
