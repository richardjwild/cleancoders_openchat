package org.openchat.domain;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class User {

    private final String id;
    private final String username;
    private final String about;
    private final String password;

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
