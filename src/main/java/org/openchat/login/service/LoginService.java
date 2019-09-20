package org.openchat.login.service;

import org.openchat.users.domain.User;
import org.openchat.users.service.UsersService;

import java.util.Optional;

public class LoginService {

    private final UsersService usersService;

    public LoginService(UsersService userService) {
        this.usersService = userService;
    }

    public Optional<User> getValidLogin(String username, String password) {
        return usersService.findUser(username)
                .filter(user -> user.getPassword().equals(password));
    }
}
