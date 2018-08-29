package org.openchat.environment;

import java.util.UUID;

public class PostIdGenerator {
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
