package dao.repository;

import dao.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final List<User> users;

    public UserRepository() {
        users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }
}
