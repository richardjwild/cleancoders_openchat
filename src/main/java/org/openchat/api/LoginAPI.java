package org.openchat.api;

import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;

public class LoginAPI {

    public String login(Request request, Response response) {
        response.status(NOT_FOUND_404);
        return "Invalid credentials.";
    }
}
