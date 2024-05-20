package org.socialnetworking;


import org.socialnetworking.domain.CommandEntered;
import org.socialnetworking.domain.OutputMessage;
import org.socialnetworking.domain.Posted;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Comparator.comparing;
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
            case PostCommand postCommand -> addToTimeline(postCommand);
            case ReadCommand readCommand -> readTimeline(readCommand);
            case WallCommand wallCommand -> wall(wallCommand);
            case FollowCommand followCommand -> follow(followCommand);
        };
    }

    private OutputMessage follow(FollowCommand followCommand) {
        repository.follows(followCommand.subscriber(), followCommand.target());
        return new OutputMessage(List.of());
    }

    private OutputMessage wall(WallCommand wallCommand) {
        List<String> wall = repository.wallFor(wallCommand.user())
                .stream()
                .sorted(comparing(Posted::timestamp).reversed())
                .map(post -> "%s - %s (%s)".formatted(post.userName(), post.message(), howLongAgoPosted(post)))
                .toList();
        return new OutputMessage(wall);
    }

    //todo - this code should probably be in a view layer rather than here
    public String howLongAgoPosted(Posted posted) {
        final var difference  = Duration.between(posted.timestamp(), eventTimeSupplier.get());

        return switch (difference) {
            case Duration d when d.toDays() > 0 -> d.toDays() + " days ago";
            case Duration d when d.toHours() > 0 -> d.toHours() + " hours ago";
            //todo - handle cases where day, hour and seconds are one so text should use the singular
            case Duration d when d.toMinutes() == 1 -> "1 minute ago";
            case Duration d when d.toMinutes() > 1 -> d.toMinutes() + " minutes ago";
            default ->  difference.toSeconds() + " seconds ago";
        };
    }

    private OutputMessage addToTimeline(PostCommand postCommand) {
        repository.addToTimeline(new Posted(postCommand.userName(), postCommand.message(), eventTimeSupplier.get()));
        return new OutputMessage(List.of());
    }

    private OutputMessage readTimeline(ReadCommand readCommand) {
        return new OutputMessage(repository.fetchTimeline(readCommand.userName()).messages());
    }

}
