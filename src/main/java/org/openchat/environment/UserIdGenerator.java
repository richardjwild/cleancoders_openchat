package org.openchat.environment;

import java.util.UUID;

public class UserIdGenerator {
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
