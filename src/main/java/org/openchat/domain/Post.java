package org.openchat.domain;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class Post {

    private final String userId;
    private final String text;
    private final LocalDateTime dateTime;
    private final String postId;

    public Post(String userId, String text, LocalDateTime dateTime, String postId) {
        this.userId = userId;
        this.text = text;
        this.dateTime = dateTime;
        this.postId = postId;
    }

    public String text() {
        return text;
    }

    public LocalDateTime dateTime() {
        return dateTime;
    }

    public String userId() {
        return userId;
    }

    public String postId() {
        return postId;
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
        return "Post{" +
                "userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                ", postId='" + postId + '\'' +
                '}';
    }
}
