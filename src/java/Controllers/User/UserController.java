package Controllers.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    private static final String LOGIN_PAGE = "Login.html";
    private static final String SEARCH_CONTROLLER = "SearchController";
    private static final String CREATE_CONTROLLER = "CreateController";
    private static final String DELETE_CONTROLLER = "DeleteController";
    private static final String UPDATE_CONTROLLER = "UpdateController";
    private static final String DETAILS_CONTROLLER = "UserDetailsController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String url = LOGIN_PAGE;

        if (action == null || action.trim().isEmpty()) {
            action = "Search";
        }

        if ("Search".equalsIgnoreCase(action)) {
            url = SEARCH_CONTROLLER;
        } else if ("Create".equalsIgnoreCase(action)) {
            url = CREATE_CONTROLLER;
        } else if ("Delete".equalsIgnoreCase(action)) {
            url = DELETE_CONTROLLER;
        } else if ("Update".equalsIgnoreCase(action)) {
            url = UPDATE_CONTROLLER;
        } else if ("Details".equalsIgnoreCase(action)) {
            url = DETAILS_CONTROLLER;
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
