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
import java.util.List;

@WebServlet(name = "SearchController", urlPatterns = {"/SearchController"})
public class SearchController extends HttpServlet {

    private static final String SEARCH_PAGE = "Search.jsp";
    private static final String MESSAGE_PAGE = "DisplayMessage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String url = SEARCH_PAGE;

        try {
            HttpSession session = request.getSession(false);
            User userLoggedIn = session == null ? null : (User) session.getAttribute("UserLoggedIn");

            if (userLoggedIn == null || !userLoggedIn.isAdmin()) {
                request.setAttribute("action", "Search user");
                request.setAttribute("page", "Login.html");
                request.setAttribute("message", "Please login by admin account.");
                url = MESSAGE_PAGE;
            } else {
                String searchValue = request.getParameter("txtSearchValue");
                if (searchValue == null) {
                    Object oldValue = request.getAttribute("txtSearchValue");
                    searchValue = oldValue == null ? "" : oldValue.toString();
                }

                UserDAO userDAO = new UserDAO();
                List<User> userList = userDAO.searchUsersByLastName(searchValue);

                request.setAttribute("SearchResult", userList);
                request.setAttribute("txtSearchValue", searchValue);
            }
        } catch (Exception ex) {
            request.setAttribute("action", "Search user");
            request.setAttribute("page", "Search.jsp");
            request.setAttribute("message", "Error: " + ex.getMessage());
            url = MESSAGE_PAGE;
            log("SearchController error: " + ex.getMessage(), ex);
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
