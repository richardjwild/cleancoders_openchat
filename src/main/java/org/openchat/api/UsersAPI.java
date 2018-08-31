package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.Post;
import org.openchat.domain.PostService;
import org.openchat.domain.User;
import spark.Request;
import spark.Response;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.eclipse.jetty.http.HttpStatus.CREATED_201;
import static org.eclipse.jetty.http.HttpStatus.OK_200;

public class UsersAPI {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private final PostService postService;
    private final UserService userService;

    public UsersAPI(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    public String retrievePosts(Request request, Response response) {
        String userId = request.params("userId");
        JsonArray json = new JsonArray();
        for (Post post : postService.timelineFor(userId)) {
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

    public String registerNewUser(Request request, Response response) {
        JsonObject body = Json.parse(request.body()).asObject();
        String username = body.getString("username", null);
        String about = body.getString("about", null);
        String password = body.getString("password", null);
        User user = userService.createUser(username, about, password);
        response.status(CREATED_201);
        response.type(ContentType.APPLICATION_JSON);
        return jsonUser(user).toString();
    }

    public String retrieveUsers(Request unused, Response response) {
        response.status(OK_200);
        response.type(ContentType.APPLICATION_JSON);
        JsonArray json = new JsonArray();
        userService.allUsers().stream()
                .map(this::jsonUser)
                .forEach(json::add);
        return json.toString();
    }

    private JsonObject jsonPost(Post post) {
        return new JsonObject()
                .add("text", post.text())
                .add("dateTime", post.dateTime().format(DATETIME_FORMATTER))
                .add("userId", post.userId())
                .add("postId", post.postId());
    }

    private JsonObject jsonUser(User user) {
        return new JsonObject()
                .add("id", user.id())
                .add("username", user.username())
                .add("about", user.about());
    }
}
