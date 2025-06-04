package lk.jiat.ee.core.dao;

import lk.jiat.ee.core.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final List<UserDTO> users = new ArrayList<>();
    private static int idCounter = 1;

    public static UserDTO getUser(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<UserDTO> getAllUsers() {
        return new ArrayList<>(users);
    }

    public static UserDTO userRegister(UserDTO user) {
            if (emailExists(user.getEmail())) {
                return null;
            }
            user.setId(idCounter++);
            users.add(user);
            return user;
    }

    public static UserDTO userLogin(String username, String password) {
        return users.stream()
                .filter(user ->
                        username != null && password != null &&
                        username.equals(user.getUsername()) &&
                        password.equals(user.getPassword()))
                .findFirst()
                .orElse(null);
    }

    public static boolean emailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public static boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

}
