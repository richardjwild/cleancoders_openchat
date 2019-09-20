package org.openchat.login.api;

import org.json.JSONObject;
import org.openchat.login.service.LoginService;
import org.openchat.users.domain.User;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class LoginController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public Object login(Request req, Response res) {
        JSONObject requestBody = new JSONObject(req.body());
        String username = requestBody.getString("username");
        String password = requestBody.getString("password");
        Optional<User> loggedInUser = loginService.getValidLogin(username, password);
        if (loggedInUser.isPresent()) {
            User user = loggedInUser.get();
            res.status(200);
            res.type("application/json");
            JSONObject response = new JSONObject();
            response.put("id", user.getId().toString());
            response.put("username", user.getName());
            response.put("about", user.getAbout());
            return response;
        } else {
            res.status(404);
            return "Invalid credentials.";
        }
    }
}
