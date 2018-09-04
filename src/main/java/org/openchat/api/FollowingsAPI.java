package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.UserService;
import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.*;

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
        response.type(ContentType.APPLICATION_JSON);
        if (userService.isAlreadyFollowing(followerId, followeeId)) {
            response.status(BAD_REQUEST_400);
            return "Following already exists.";
        } else {
            userService.createFollowingRelationship(followerId, followeeId);
            response.status(CREATED_201);
        }
        return "Following created.";
    }
}
