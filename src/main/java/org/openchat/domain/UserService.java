package org.openchat.domain;

import org.openchat.environment.UserIdGenerator;
import org.openchat.repository.UserRepository;

import java.util.List;
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

    public Optional<User> findMatchingUser(String username, String password) {
        for (User user : userRepository.retrieveUsers()) {
            if (user.nameMatches(username) && user.credentialsMatch(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public List<User> allUsers() {
        return userRepository.retrieveUsers();
    }

    public Optional<User> findUser(String followerId) {
        return userRepository.retrieveUsers().stream()
                .filter(user -> user.is(followerId))
                .findFirst();
    }

    public void saveUser(User user) {
        userRepository.updateUser(user);
    }
}