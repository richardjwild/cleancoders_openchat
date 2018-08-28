package org.openchat.api;

import java.util.UUID;

public class PostIdGenerator {
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
