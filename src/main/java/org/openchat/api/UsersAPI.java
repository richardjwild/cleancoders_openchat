package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsersAPI {

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss'Z'");

    private String text;
    private Clock clock;
    private PostIdGenerator postIdGenerator;
    private String userId;
    private LocalDateTime dateTime;

    public UsersAPI(Clock clock, PostIdGenerator postIdGenerator) {
        this.clock = clock;
        this.postIdGenerator = postIdGenerator;
    }

    public String retrievePosts(Request request, Response response) {
        JsonArray json = new JsonArray();
        if (text != null) {
            JsonObject post = new JsonObject();
            post.add("text", text);
            post.add("dateTime", format(dateTime));
            post.add("userId", userId);
            post.add("postId", postIdGenerator.nextId());
            json.add(post);
        }
        response.type("application/json");
        response.status(200);
        return json.toString();
    }

    private String format(LocalDateTime now) {
        return now.format(DATETIME_FORMATTER);
    }

    public String createPost(Request request, Response response) {
        this.userId = request.params("userId");
        JsonObject json = Json.parse(request.body()).asObject();
        this.text = json.getString("text", null);
        this.dateTime = clock.now();
        response.status(201);
        response.type("text/plain");
        JsonObject post = new JsonObject();
        post.add("text", text);
        post.add("dateTime", format(dateTime));
        post.add("userId", userId);
        post.add("postId", postIdGenerator.nextId());
        return post.toString();
    }
}
