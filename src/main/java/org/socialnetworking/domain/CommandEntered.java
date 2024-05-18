package org.socialnetworking.domain;

import static org.socialnetworking.domain.CommandEntered.*;

public sealed interface CommandEntered permits Follows, Post, Read, Wall {
    record Post(String userName, String message) implements CommandEntered {
    }

    record Read(String userName) implements CommandEntered {
    }

    record Follows(String subscriber, String target) implements CommandEntered {
    }

    record Wall(String user) implements CommandEntered {
    }
}
