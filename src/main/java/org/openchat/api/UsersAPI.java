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
import java.util.function.BinaryOperator;

import static org.eclipse.jetty.http.HttpStatus.CREATED_201;
import static org.eclipse.jetty.http.HttpStatus.OK_200;

public class UsersAPI extends OpenChatAPI {

    private final PostService postService;
    private final UserService userService;

    public UsersAPI(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    public String retrievePosts(Request request, Response response) {
        String userId = request.params("userId");
        response.type(ContentType.APPLICATION_JSON);
        response.status(OK_200);
        return postService.timelineFor(userId).stream()
                .map(this::jsonPost)
                .reduce(new JsonArray(), JsonArray::add, UNUSED_COMBINER)
                .toString();
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
        return userService.allUsers().stream()
                .map(this::jsonUser)
                .reduce(new JsonArray(), JsonArray::add, UNUSED_COMBINER)
                .toString();
    }
}
