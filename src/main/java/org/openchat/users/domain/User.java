package org.openchat.users.domain;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String name;
    private final String password;
    private final String about;

    public User(UUID id, String name, String password, String about) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.about = about;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getPassword() {
        return password;
    }
}
