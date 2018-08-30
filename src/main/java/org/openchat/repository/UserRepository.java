package org.openchat.repository;

import org.openchat.domain.User;

public interface UserRepository {
    void storeUser(User user);
}
