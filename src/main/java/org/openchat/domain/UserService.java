package org.openchat.domain;

import org.openchat.environment.UserIdGenerator;
import org.openchat.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

    public void createFollowingRelationship(String followerId, String followeeId) {
        findUser(followerId).ifPresent(follower -> {
            findUser(followeeId).ifPresent(followee -> {
                follower.follow(followee);
                saveUser(follower);
            });
        });
    }

    public boolean isAlreadyFollowing(String followerId, String followeeId) {
        return findUser(followerId)
                .flatMap(follower -> findUser(followeeId).map(isFollowedBy(follower)))
                .orElse(false);
    }

    private Function<User, Boolean> isFollowedBy(User follower) {
        return followee -> follower.usersFollowing().contains(followee);
    }

    public Collection<User> usersFollowing(String followerId) {
        return findUser(followerId)
                .map(User::usersFollowing)
                .orElseGet(Collections::emptyList);
    }
}