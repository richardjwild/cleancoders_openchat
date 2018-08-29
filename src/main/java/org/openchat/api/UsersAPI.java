package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.Post;
import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsersAPI {

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss'Z'");

    private Clock clock;
    private PostIdGenerator postIdGenerator;

    private Post post;

    public UsersAPI(Clock clock, PostIdGenerator postIdGenerator) {
        this.clock = clock;
        this.postIdGenerator = postIdGenerator;
    }

    public String retrievePosts(Request request, Response response) {
        JsonArray json = new JsonArray();
        if (post != null) {
            json.add(jsonPost(post));
        }
        response.type("application/json");
        response.status(200);
        return json.toString();
    }

    public String createPost(Request request, Response response) {
        JsonObject jsonBody = Json.parse(request.body()).asObject();
        this.post = new Post(
                request.params("userId"),
                jsonBody.getString("text", null),
                clock.now(),
                postIdGenerator.nextId());
        response.status(201);
        response.type("text/plain");
        return jsonPost(post).toString();
    }

    private JsonObject jsonPost(Post post) {
        return new JsonObject()
                .add("text", post.text())
                .add("dateTime", format(post.dateTime()))
                .add("userId", post.userId())
                .add("postId", post.postId());
    }

    private String format(LocalDateTime now) {
        return now.format(DATETIME_FORMATTER);
    }
}
