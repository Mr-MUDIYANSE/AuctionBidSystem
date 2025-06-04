package lk.jiat.ee.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.ee.core.dto.UserDTO;
import lk.jiat.ee.ejb.remote.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/users")
public class Users extends HttpServlet {

    @EJB
    private UserService userService;

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        PrintWriter out = response.getWriter();

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                UserDTO user = userService.getUserById(id);

                if (user != null) {
                    user.setPassword(null);

                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.add("user", gson.toJsonTree(user));
                    out.print(gson.toJson(jsonResponse));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonObject errorResponse = new JsonObject();
                    errorResponse.addProperty("status", "error");
                    errorResponse.addProperty("message", "User not found.");
                    out.print(gson.toJson(errorResponse));
                }

            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("status", "error");
                errorResponse.addProperty("message", "Invalid ID format.");
                out.print(gson.toJson(errorResponse));
            }

        } else {
            // Return all users
            List<UserDTO> users = userService.getAllUsers();
            for (UserDTO user : users) {
                user.setPassword(null);
            }

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "success");
            jsonResponse.add("users", gson.toJsonTree(users));
            out.print(gson.toJson(jsonResponse));
        }

        out.flush();
    }
}
