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

@WebServlet(name = "CreateController", urlPatterns = {"/CreateController"})
public class CreateController extends HttpServlet {

    private static final String CREATE_PAGE = "CreateUser.jsp";
    private static final String MESSAGE_PAGE = "DisplayMessage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String userName = request.getParameter("txtUserName");
        String password = request.getParameter("txtPassword");
        String lastName = request.getParameter("txtLastName");
        boolean isAdmin = request.getParameter("chkIsAdmin") != null;

        if (userName == null && password == null && lastName == null) {
            RequestDispatcher rd = request.getRequestDispatcher(CREATE_PAGE);
            rd.forward(request, response);
            return;
        }

        String url = MESSAGE_PAGE;
        String message;

        try {
            if (isBlank(userName) || isBlank(password) || isBlank(lastName)) {
                message = "Please input UserName, Password and LastName.";
                request.setAttribute("page", CREATE_PAGE);
            } else {
                UserDAO userDAO = new UserDAO();

                if (userDAO.getUserByUserName(userName) != null) {
                    message = "UserName '" + userName + "' already exists.";
                    request.setAttribute("page", CREATE_PAGE);
                } else {
                    User user = new User(userName.trim(), password.trim(), lastName.trim(), isAdmin);
                    boolean result = userDAO.addUser(user);
                    message = result ? "User has been added successfully." : "Add user failed.";

                    HttpSession session = request.getSession(false);
                    User loggedIn = session == null ? null : (User) session.getAttribute("UserLoggedIn");
                    if (loggedIn != null && loggedIn.isAdmin()) {
                        request.setAttribute("page", "UserController?action=Search");
                    } else {
                        request.setAttribute("page", "Login.html");
                    }
                }
            }

            request.setAttribute("action", "Create User");
            request.setAttribute("message", message);
        } catch (Exception ex) {
            request.setAttribute("action", "Create User");
            request.setAttribute("page", CREATE_PAGE);
            request.setAttribute("message", "Error: " + ex.getMessage());
            log("CreateController error: " + ex.getMessage(), ex);
        }

        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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
