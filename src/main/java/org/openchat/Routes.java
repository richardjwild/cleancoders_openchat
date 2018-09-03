package org.openchat;

import org.openchat.api.FollowingsAPI;
import org.openchat.api.LoginAPI;
import org.openchat.api.UserService;
import org.openchat.api.UsersAPI;
import org.openchat.domain.PostService;
import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;
import org.openchat.environment.UserIdGenerator;
import org.openchat.repository.InMemoryRepository;

import static spark.Spark.*;

public class Routes {

    private UsersAPI usersAPI;
    private LoginAPI loginAPI;
    private FollowingsAPI followingsAPI;

    public void create() {
        createAPIs();
        swaggerRoutes();
        openchatRoutes();
    }

    private void createAPIs() {
        InMemoryRepository repository = new InMemoryRepository();
        PostService postService = new PostService(new Clock(), new PostIdGenerator(), repository);
        UserService userService = new UserService(new UserIdGenerator(), repository);
        usersAPI = new UsersAPI(postService, userService);
        loginAPI = new LoginAPI(userService);
        followingsAPI = new FollowingsAPI(userService);
    }

    private void openchatRoutes() {
        get("status", (req, res) -> "OpenChat: OK!");
        post("login", (req, res) -> loginAPI.login(req, res));
        get("users", (req, res) -> usersAPI.retrieveUsers(req, res));
        post("users", (req, res) -> usersAPI.registerNewUser(req, res));
        post("users/:userId/timeline", (req, res) -> usersAPI.createPost(req, res));
        get("users/:userId/timeline", (req, res) -> usersAPI.retrievePosts(req, res));
        get("users/:userId/wall", (req, res) -> usersAPI.wall(req, res));
        get("followings/:followerId/followees", (req, res) -> followingsAPI.usersFollowing(req, res));
        post("followings", (req, res) -> followingsAPI.createFollowingRelationship(req, res));
    }

    private void swaggerRoutes() {
        options("users", (req, res) -> "OK");
        options("login", (req, res) -> "OK");
        options("users/:userId/timeline", (req, res) -> "OK");
        options("followings", (req, res) -> "OK");
        options("followings/:userId/followees", (req, res) -> "OK");
        options("users/:userId/wall", (req, res) -> "OK");
    }
}
