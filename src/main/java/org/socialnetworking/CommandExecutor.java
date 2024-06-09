package org.socialnetworking;


import org.socialnetworking.domain.*;

import java.time.Instant;
import java.util.function.Supplier;

import static org.socialnetworking.domain.CommandEntered.*;

public class CommandExecutor {
    private final Repository repository;

    private final Supplier<Instant> eventTimeSupplier;


    public CommandExecutor(Repository repository, Supplier<Instant> eventTimeSupplier) {
        this.repository = repository;
        this.eventTimeSupplier = eventTimeSupplier;
    }

    public Response execute(CommandEntered commandEntered) {
        return switch(commandEntered) {
            case PostCommand postCommand -> addToTimeline(postCommand);
            case ReadCommand readCommand -> readTimeline(readCommand);
            case WallCommand wallCommand -> wall(wallCommand);
            case FollowCommand followCommand -> follow(followCommand);
        };
    }

    private VoidResponse follow(FollowCommand followCommand) {
        repository.follows(followCommand.subscriber(), followCommand.target());
        return new VoidResponse();
    }

    private Wall wall(WallCommand wallCommand) {
        return repository.wallFor(wallCommand.user());
    }

    private VoidResponse addToTimeline(PostCommand postCommand) {
        repository.addToTimeline(new Posted(postCommand.userName(), postCommand.message(), eventTimeSupplier.get()));
        return new VoidResponse();
    }

    private Timeline readTimeline(ReadCommand readCommand) {
        return repository.fetchTimeline(readCommand.userName());
    }

}
