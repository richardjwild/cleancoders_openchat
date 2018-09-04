package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.UserService;
import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.*;

public class FollowingsAPI extends OpenChatAPI {

    private static final String FOLLOWING_ALREADY_EXISTS = "Following already exists.";
    private static final String FOLLOWING_CREATED = "Following created.";

    private UserService userService;

    public FollowingsAPI(UserService userService) {
        this.userService = userService;
    }

    public String usersFollowing(Request request, Response response) {
        String followerId = request.params("followerId");
        response.status(OK_200);
        response.type(ContentType.APPLICATION_JSON);
        return userService.usersFollowing(followerId).stream()
                .map(this::jsonUser)
                .reduce(new JsonArray(), JsonArray::add, UNUSED_COMBINER)
                .toString();
    }

    public String createFollowingRelationship(Request request, Response response) {
        JsonObject body = Json.parse(request.body()).asObject();
        String followerId = body.getString("followerId", null);
        String followeeId = body.getString("followeeId", null);
        response.type(ContentType.APPLICATION_JSON);
        if (userService.isAlreadyFollowing(followerId, followeeId)) {
            response.status(BAD_REQUEST_400);
            return FOLLOWING_ALREADY_EXISTS;
        } else {
            userService.createFollowingRelationship(followerId, followeeId);
            response.status(CREATED_201);
        }
        return FOLLOWING_CREATED;
    }
}
