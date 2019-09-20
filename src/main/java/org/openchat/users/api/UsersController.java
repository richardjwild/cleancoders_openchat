package org.openchat.users.api;

import org.json.JSONObject;
import org.openchat.users.service.UsersService;
import org.openchat.users.domain.User;
import spark.Request;
import spark.Response;

public class UsersController {

    private UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    public Object create(Request req, Response res) {
        JSONObject requestBody = new JSONObject(req.body());
        String username = requestBody.getString("username");
        String password = requestBody.getString("password");
        String about = requestBody.getString("about");
        User user = usersService.registerNewUser(username, password, about);
        JSONObject response = new JSONObject();
        response.put("id", user.getId());
        response.put("username", user.getName());
        response.put("about", user.getAbout());
        res.status(201);
        res.type("application/json");
        return response;
    }
}
