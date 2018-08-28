package org.openchat.api;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class UsersAPI {

    private String text;

    public String retrievePosts() {
        JsonArray json = new JsonArray();
        if (text != null) {
            JsonObject post = new JsonObject();
            post.add("text", text);
            json.add(post);
        }
        return json.toString();
    }

    public void createPost(String text) {
        this.text = text;
    }
}
