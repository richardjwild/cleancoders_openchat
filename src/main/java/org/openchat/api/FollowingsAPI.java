package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.User;
import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.CREATED_201;
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

    public String createFollowingRelationship(Request request, Response response) {
        JsonObject body = Json.parse(request.body()).asObject();
        String followerId = body.getString("followerId", null);
        String followeeId = body.getString("followeeId", null);
        userService.findUser(followerId).ifPresent(follower -> {
            userService.findUser(followeeId).ifPresent(followee -> {
                follower.follow(followee);
                userService.saveUser(follower);
            });
        });
        response.status(CREATED_201);
        response.type(ContentType.APPLICATION_JSON);
        return "";
    }
}
