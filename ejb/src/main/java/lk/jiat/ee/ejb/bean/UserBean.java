package lk.jiat.ee.ejb.bean;

import jakarta.ejb.Stateful;
import lk.jiat.ee.core.dao.UserDAO;
import lk.jiat.ee.core.dto.UserDTO;
import lk.jiat.ee.ejb.remote.UserService;

import java.io.Serializable;
import java.util.List;

@Stateful
public class UserBean implements UserService, Serializable {

    @Override
    public UserDTO getUserById(int id) {
        return UserDAO.getUser(id);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return UserDAO.getAllUsers();
    }

    @Override
    public UserDTO addUser(UserDTO user) {
        return UserDAO.userRegister(user);
    }

    @Override
    public void updateUser(UserDTO user) {

    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public UserDTO loginUser(String username, String password) {
        return UserDAO.userLogin(username, password);
    }
}
