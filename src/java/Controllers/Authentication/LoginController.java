package Controllers.Authentication;

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

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private static final String LOGIN_PAGE = "Login.html";
    private static final String MESSAGE_PAGE = "DisplayMessage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String userName = request.getParameter("txtUserName");
        String password = request.getParameter("txtPassword");

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.checkLogin(userName, password);

            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("UserLoggedIn", user);

                if (user.isAdmin()) {
                    response.sendRedirect("UserController?action=Search");
                } else {
                    response.sendRedirect("UserController?action=Details&txtUserName=" + user.getUserName());
                }
                return;
            }

            request.setAttribute("action", "Login to website");
            request.setAttribute("page", LOGIN_PAGE);
            request.setAttribute("message", "The user name or password is invalid.");
        } catch (Exception ex) {
            request.setAttribute("action", "Login to website");
            request.setAttribute("page", LOGIN_PAGE);
            request.setAttribute("message", "Error: " + ex.getMessage());
            log("LoginController error: " + ex.getMessage(), ex);
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
