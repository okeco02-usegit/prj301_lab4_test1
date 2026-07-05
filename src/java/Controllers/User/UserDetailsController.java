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

@WebServlet(name = "UserDetailsController", urlPatterns = {"/UserDetailsController"})
public class UserDetailsController extends HttpServlet {

    private static final String DETAILS_PAGE = "UserDetails.jsp";
    private static final String MESSAGE_PAGE = "DisplayMessage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String url = DETAILS_PAGE;

        try {
            HttpSession session = request.getSession(false);
            User userLoggedIn = session == null ? null : (User) session.getAttribute("UserLoggedIn");

            if (userLoggedIn == null) {
                request.setAttribute("action", "User Details");
                request.setAttribute("page", "Login.html");
                request.setAttribute("message", "Please login first.");
                url = MESSAGE_PAGE;
            } else {
                String userName = request.getParameter("txtUserName");

                if (!userLoggedIn.isAdmin()) {
                    userName = userLoggedIn.getUserName();
                }

                if (userName == null || userName.trim().isEmpty()) {
                    userName = userLoggedIn.getUserName();
                }

                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserByUserName(userName);

                if (user == null) {
                    request.setAttribute("action", "User Details");
                    request.setAttribute("page", "UserController?action=Search");
                    request.setAttribute("message", "User not found.");
                    url = MESSAGE_PAGE;
                } else {
                    request.setAttribute("UserDetails", user);
                }
            }
        } catch (Exception ex) {
            request.setAttribute("action", "User Details");
            request.setAttribute("page", "UserController?action=Search");
            request.setAttribute("message", "Error: " + ex.getMessage());
            url = MESSAGE_PAGE;
            log("UserDetailsController error: " + ex.getMessage(), ex);
        }

        RequestDispatcher rd = request.getRequestDispatcher(url);
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
