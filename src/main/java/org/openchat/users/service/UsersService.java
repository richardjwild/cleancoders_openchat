package org.openchat.users.service;

import org.openchat.users.repository.UsersRepository;
import org.openchat.users.domain.User;

import java.util.Optional;
import java.util.UUID;

public class UsersService {

    private UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User registerNewUser(String username, String password, String about) {
        User user = new User(UUID.randomUUID(), username, password, about);
        usersRepository.save(user);
        return user;
    }

    public Optional<User> findUser(String username) {
        return usersRepository.findByName(username);
    }
}
