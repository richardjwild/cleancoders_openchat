package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.Post;
import org.openchat.domain.PostService;
import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.eclipse.jetty.http.HttpStatus.CREATED_201;
import static org.eclipse.jetty.http.HttpStatus.OK_200;

public class UsersAPI {

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public UsersAPI(PostService postService) {
        this.postService = postService;
    }

    private final PostService postService;

    public String retrievePosts(Request request, Response response) {
        JsonArray json = new JsonArray();
        for (Post post : postService.retrievePosts()) {
            json.add(jsonPost(post));
        }
        response.type(ContentType.APPLICATION_JSON);
        response.status(OK_200);
        return json.toString();
    }

    public String createPost(Request request, Response response) {
        JsonObject jsonBody = Json.parse(request.body()).asObject();
        String userId = request.params("userId");
        String text = jsonBody.getString("text", null);
        Post post = postService.createPost(userId, text);
        response.status(CREATED_201);
        response.type(ContentType.APPLICATION_JSON);
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
