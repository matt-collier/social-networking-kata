package org.socialnetworking;


import org.socialnetworking.domain.CommandEntered;
import org.socialnetworking.domain.OutputMessage;
import org.socialnetworking.domain.Posted;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

import static org.socialnetworking.domain.CommandEntered.*;

public class CommandExecutor {
    private final Repository repository;

    private final Supplier<Instant> eventTimeSupplier;


    public CommandExecutor(Repository repository, Supplier<Instant> eventTimeSupplier) {
        this.repository = repository;
        this.eventTimeSupplier = eventTimeSupplier;
    }

    public OutputMessage execute(CommandEntered commandEntered) {
        return switch(commandEntered) {
            case Post postCommand -> addToTimeline(postCommand);
            case Read readCommand -> readTimeline(readCommand);
            case Wall wallCommand -> wall(wallCommand);
            case Follows followCommand -> follow(followCommand);
        };
    }

    private OutputMessage follow(Follows followCommand) {
        return repository.follows(followCommand.subscriber(), followCommand.target());
    }

    private OutputMessage wall(Wall wallCommand) {
        List<String> wall = repository.wallFor(wallCommand.user())
                .stream()
                .map(post -> "%s - %s (%s)".formatted(post.userName(), post.message(), howLongAgoPosted(post)))
                .toList();
        return new OutputMessage(wall);
    }

    private String howLongAgoPosted(Posted post) {
        final var difference  = Duration.between(post.timestamp(), eventTimeSupplier.get());
        final long days = difference.toDays();
        if (days > 0) {
            return days + " days ago";
        }
        final long hours = difference.toHours();
        if (hours > 0) {
            return hours + " hours ago";
        }
        final long minutes = difference.toMinutes();
        if (minutes > 0) {
            return minutes + " minutes ago";
        }
        return difference.toSeconds() + " seconds ago";


    }

    private OutputMessage addToTimeline(Post post) {
        repository.addToTimeline(new Posted(post.userName(), post.message(), eventTimeSupplier.get()));
        return new OutputMessage(List.of());
    }

    private OutputMessage readTimeline(Read read) {
        return new OutputMessage(repository.fetchTimeline(read.userName()));
    }

}
