package org.socialnetworking;


import org.socialnetworking.domain.CommandEntered;
import org.socialnetworking.domain.OutputMessage;

import java.util.List;

import static org.socialnetworking.domain.CommandEntered.*;

public class CommandExecutor {
    private final Repository repository;

    public CommandExecutor(final Repository repository) {
        this.repository = repository;
    }

    public OutputMessage execute(final CommandEntered commandEntered) {
        return switch(commandEntered) {
            case Post postCommand -> addToTimeline(postCommand);
            case Read readCommand -> readTimeline(readCommand);
            case Wall wallCommand -> wall(wallCommand);
            default -> throw new IllegalStateException("Unexpected value: " + commandEntered);
        };
    }

    private OutputMessage wall(final Wall wallCommand) {
        final List<String> wall = repository.wallFor(wallCommand.user())
                .stream()
                .map(post -> post.userName() + " - " + post.message())
                .toList();
        return new OutputMessage(wall);
    }

    private OutputMessage addToTimeline(Post post) {
        repository.addToTimeline(post.userName(), post.message());
        return new OutputMessage(List.of());
    }

    private OutputMessage readTimeline(Read read) {
        return new OutputMessage(repository.fetchTimeline(read.userName()));
    }

}
