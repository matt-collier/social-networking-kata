package org.socialnetworking.domain;

import static org.socialnetworking.domain.CommandEntered.*;

public sealed interface CommandEntered permits FollowCommand, PostCommand, ReadCommand, WallCommand {
    record PostCommand(String userName, String message) implements CommandEntered {
    }

    record ReadCommand(String userName) implements CommandEntered {
    }

    record FollowCommand(String subscriber, String target) implements CommandEntered {
    }

    record WallCommand(String user) implements CommandEntered {
    }
}
