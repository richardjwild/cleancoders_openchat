package org.openchat.api;

import com.eclipsesource.json.JsonArray;
import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.OK_200;

public class FollowingsAPI extends OpenChatAPI {

    private UserService userService;

    public FollowingsAPI(UserService userService) {
        this.userService = userService;
    }

    public String usersFollowing(Request request, Response response) {
        String followerId = request.params("followerId");
        JsonArray json = new JsonArray();
        response.status(OK_200);
        response.type(ContentType.APPLICATION_JSON);
        userService.findUser(followerId).ifPresent(follower ->
                follower.usersFollowing().stream()
                        .map(this::jsonUser)
                        .forEach(json::add));
        return json.toString();
    }
}
