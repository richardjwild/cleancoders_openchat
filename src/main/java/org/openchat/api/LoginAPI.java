package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.User;
import org.openchat.domain.UserService;
import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.eclipse.jetty.http.HttpStatus.OK_200;

public class LoginAPI {

    private UserService userService;

    public LoginAPI(UserService userService) {
        this.userService = userService;
    }

    public String login(Request request, Response response) {
        JsonObject body = Json.parse(request.body()).asObject();
        String username = body.getString("username", null);
        String password = body.getString("password", null);
        return userService.findMatchingUser(username, password)
                .map(user -> validLoginResponse(response, user))
                .orElseGet(() -> invalidLoginResponse(response));
    }

    private String validLoginResponse(Response response, User user) {
        response.status(OK_200);
        response.type(ContentType.APPLICATION_JSON);
        return new JsonObject()
                .add("id", user.id())
                .add("username", user.username())
                .add("about", user.about())
                .toString();
    }

    private String invalidLoginResponse(Response response) {
        response.status(NOT_FOUND_404);
        return "Invalid credentials.";
    }
}
