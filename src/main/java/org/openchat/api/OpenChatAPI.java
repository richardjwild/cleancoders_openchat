package org.openchat.api;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.Post;
import org.openchat.domain.User;

import java.time.format.DateTimeFormatter;
import java.util.function.BinaryOperator;

abstract class OpenChatAPI {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static final BinaryOperator<JsonArray> UNUSED_COMBINER = (a, b) -> {
        throw new UnsupportedOperationException("To use a parallel stream this combiner must be implemented");
    };

    JsonObject jsonPost(Post post) {
        return new JsonObject()
                .add("text", post.text())
                .add("dateTime", post.dateTime().format(DATETIME_FORMATTER))
                .add("userId", post.userId())
                .add("postId", post.postId());
    }

    JsonObject jsonUser(User user) {
        return new JsonObject()
                .add("id", user.id())
                .add("username", user.username())
                .add("about", user.about());
    }
}
