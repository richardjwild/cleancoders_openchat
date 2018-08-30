package org.openchat.repository;

import org.openchat.domain.User;

import java.util.List;

public interface UserRepository {

    void storeUser(User user);

    List<User> retrieveUsers();
}
