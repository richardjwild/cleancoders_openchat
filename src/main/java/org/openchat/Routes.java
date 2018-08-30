package org.openchat;

import org.openchat.api.UserService;
import org.openchat.api.UsersAPI;
import org.openchat.domain.PostService;
import org.openchat.dummy.DummyLoginAPI;
import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;
import org.openchat.environment.UserIdGenerator;
import org.openchat.repository.InMemoryRepository;

import static spark.Spark.*;

public class Routes {

    private UsersAPI usersAPI;
    private DummyLoginAPI dummyLoginAPI;

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
        dummyLoginAPI = new DummyLoginAPI();
    }

    private void openchatRoutes() {
        get("status", (req, res) -> "OpenChat: OK!");
        post("login", (req, res) -> dummyLoginAPI.login(req, res));
        post("users/:userId/timeline", (req, res) -> usersAPI.createPost(req, res));
        get("users/:userId/timeline", (req, res) -> usersAPI.retrievePosts(req, res));
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
