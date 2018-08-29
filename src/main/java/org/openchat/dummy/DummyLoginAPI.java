package org.openchat.dummy;

import spark.Request;
import spark.Response;

import java.util.UUID;

public class DummyLoginAPI {

    public String login(Request request, Response response) {
        response.status(200);
        response.type("application/json");
        String uuid = UUID.randomUUID().toString();
        return "{" +
                "\"id\":\"" + uuid + "\"," +
                "\"username\":\"Rich\"," +
                "\"about\":\"Fake it till you make it\"" +
                "}";
    }
}
