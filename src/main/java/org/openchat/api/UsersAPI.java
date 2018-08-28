package org.openchat.api;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsersAPI {

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss'Z'");

    private String text;
    private Clock clock;
    private String userId;

    public UsersAPI(Clock clock) {
        this.clock = clock;
    }

    public String retrievePosts() {
        JsonArray json = new JsonArray();
        if (text != null) {
            JsonObject post = new JsonObject();
            post.add("text", text);
            post.add("dateTime", format(clock.now()));
            post.add("userId", userId);
            json.add(post);
        }
        return json.toString();
    }

    private String format(LocalDateTime now) {
        return now.format(DATETIME_FORMATTER);
    }

    public void createPost(String userId, String text) {
        this.userId = userId;
        this.text = text;
    }
}
