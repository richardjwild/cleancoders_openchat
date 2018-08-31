package org.openchat.domain;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class User {

    private final String id;
    private final String username;
    private final String about;
    private final String password;
    private List<User> usersFollowing = new ArrayList<>();

    public User(String id, String username, String about, String password) {
        this.id = id;
        this.username = username;
        this.about = about;
        this.password = password;
    }

    public String id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String about() {
        return about;
    }

    public boolean nameMatches(String username) {
        return this.username.equals(username);
    }

    public boolean credentialsMatch(String password) {
        return this.password.equals(password);
    }

    public boolean is(String id) {
        return this.id.equals(id);
    }

    public void follow(User userToFollow) {
        usersFollowing.add(userToFollow);
    }

    public List<User> usersFollowing() {
        return usersFollowing;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
