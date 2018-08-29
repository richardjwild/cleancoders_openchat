package org.openchat.domain;

import java.time.LocalDateTime;

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
}
