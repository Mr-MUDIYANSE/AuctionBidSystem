package lk.jiat.ee.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.regex.Pattern;

@WebServlet("/sign-up")
public class SignUp extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        UserDTO userDTO = gson.fromJson(reader, UserDTO.class);

        System.out.println("sign up called");

        JsonObject jsonResponse = new JsonObject();
        JsonArray errors = new JsonArray();

        String username = userDTO.getUsername();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        if (username == null || username.trim().isEmpty()) {
            errors.add("Username is required.");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required.");
        } else if (!isValidEmail(email)) {
            errors.add("Invalid email format.");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.add("Password is required.");
        } else if (password.length() < 6) {
            errors.add("Password must be at least 6 characters long.");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (UserDAO.usernameExists(username)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Username already registered.");
            out.print(jsonResponse);
            out.flush();
            return;
        }

        if (UserDAO.emailExists(email)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Email already registered.");
            out.print(jsonResponse);
            out.flush();
            return;
        }

        if (errors.size() > 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.add("errors", errors);
        } else {
            UserDTO user = userService.addUser(userDTO);

            if (user != null) {
                JsonObject userJson = new JsonObject();
                userJson.addProperty("id", user.getId());
                userJson.addProperty("username", user.getUsername());
                userJson.addProperty("email", user.getEmail());

                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Registration successful.");
                jsonResponse.add("user", userJson);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Registration failed.");
            }
        }

        out.print(jsonResponse);
        out.flush();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, email);
    }
}