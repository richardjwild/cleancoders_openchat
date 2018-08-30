package org.openchat.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.User;
import org.openchat.repository.UserRepository;
import spark.Request;
import spark.Response;

import java.util.Optional;

import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.eclipse.jetty.http.HttpStatus.OK_200;

public class LoginAPI {

    private UserService userRepository;

    public LoginAPI(UserService userService) {
        this.userRepository = userService;
    }

    public String login(Request request, Response response) {
        JsonObject body = Json.parse(request.body()).asObject();
        String username = body.getString("username", null);
        return userRepository.findUser(username).map(user -> {
            response.status(OK_200);
            response.type(ContentType.APPLICATION_JSON);
            return new JsonObject()
                    .add("id", user.id())
                    .add("username", user.username())
                    .add("about", user.about())
                    .toString();
        }).orElseGet(() -> {
            response.status(NOT_FOUND_404);
            return "Invalid credentials.";
        });
    }
}
