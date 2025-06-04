package lk.jiat.ee.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.ee.core.dao.UserDAO;
import lk.jiat.ee.core.dto.UserDTO;
import lk.jiat.ee.ejb.bean.UserBean;
import lk.jiat.ee.ejb.remote.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/sign-in")
public class SignIn extends HttpServlet {

    @EJB
    private UserService userService;

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        BufferedReader reader = request.getReader();
        UserDTO loginRequest = gson.fromJson(reader, UserDTO.class);

        UserDTO user = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());

        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        if (user != null) {

            request.getSession().setAttribute("user", user);

            JsonObject obj = new JsonObject();
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "Login successful.");
            obj.addProperty("id", user.getId());
            obj.addProperty("username", user.getUsername());
            obj.addProperty("email", user.getEmail());

            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.add("user", gson.toJsonTree(obj));

            user.setPassword(null);

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Invalid username or password.");
        }

        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}