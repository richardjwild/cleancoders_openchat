package org.openchat.users.repository;

import org.openchat.users.domain.User;

import java.util.Optional;

public interface UsersRepository {
    void save(User user);

    Optional<User> findByName(String username);
}
