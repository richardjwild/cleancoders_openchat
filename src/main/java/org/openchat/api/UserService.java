package org.openchat.api;

import org.openchat.domain.User;
import org.openchat.environment.UserIdGenerator;
import org.openchat.repository.UserRepository;

import java.util.Optional;

public class UserService {

    private UserIdGenerator userIdGenerator;
    private UserRepository userRepository;

    public UserService(UserIdGenerator userIdGenerator, UserRepository userRepository) {
        this.userIdGenerator = userIdGenerator;
        this.userRepository = userRepository;
    }

    public User createUser(String username, String about, String password) {
        String userId = userIdGenerator.nextId();
        User user = new User(userId, username, about, password);
        userRepository.storeUser(user);
        return user;
    }

    public Optional<User> findUser(String username) {
        for (User user : userRepository.retrieveUsers()) {
            if (user.nameMatches(username)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}