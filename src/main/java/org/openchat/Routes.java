package org.openchat;

import org.openchat.api.UsersAPI;
import org.openchat.domain.PostService;
import org.openchat.dummy.DummyLoginAPI;
import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;

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
        PostService postService = new PostService(new Clock(), new PostIdGenerator());
        usersAPI = new UsersAPI(postService);
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
