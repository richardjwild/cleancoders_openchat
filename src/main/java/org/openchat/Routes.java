package org.openchat;

import org.openchat.login.api.LoginController;
import org.openchat.users.api.UsersController;

import static spark.Spark.*;

public class Routes {

    private UsersController usersController;
    private LoginController loginController;

    public Routes(UsersController usersController, LoginController loginController) {
        this.usersController = usersController;
        this.loginController = loginController;
    }

    public void create() {
        swaggerRoutes();
        openchatRoutes();
    }

    private void openchatRoutes() {
        get("status", (req, res) -> "OpenChat: OK!");
        post("/users", (req, res) -> usersController.create(req, res));
        post("/login", (req, res) -> loginController.login(req, res));
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
