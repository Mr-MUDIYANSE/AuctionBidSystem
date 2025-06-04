package lk.jiat.ee.ejb.remote;

import jakarta.ejb.Remote;
import lk.jiat.ee.core.dto.UserDTO;

import java.util.List;

@Remote
public interface UserService {
    UserDTO getUserById(int id);
    List<UserDTO> getAllUsers();
    UserDTO addUser(UserDTO user);
    void updateUser(UserDTO user);
    void deleteUser(int id);
    UserDTO loginUser(String username, String password);
}
