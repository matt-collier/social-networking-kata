package org.socialnetworking.domain;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.socialnetworking.domain.CommandEntered.*;

public class CommandParser {
    private static final Pattern POST = Pattern.compile("(.*) -> (.*)");
    private static final Pattern READ = Pattern.compile("^\\s*(\\S+)\\s*$");
    private static final Pattern FOLLOWING = Pattern.compile("(.*) follows (.*)");
    private static final Pattern WALL = Pattern.compile("(.*) wall");

    private static final Set<CommandMapping> commandMappings = Set.of(
            new CommandMapping(POST, CommandParser::post),
            new CommandMapping(READ, CommandParser::read),
            new CommandMapping(FOLLOWING, CommandParser::following),
            new CommandMapping(WALL, CommandParser::wall));

    public static CommandEntered commandFor(String input) {
        return commandMappings.stream()
                .map(commandParser -> commandParser.commandEntered(input))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(Function.identity())
                .orElseThrow(); //todo - not worrying about unhappy path where command is invalid or not found for purposes of this exercise
    }


    private static CommandEntered post(Matcher matcher) {
        return new PostCommand(matcher.group(1), matcher.group(2));
    }

    private static CommandEntered read(Matcher matcher) {
        return new ReadCommand(matcher.group(0));
    }

    private static CommandEntered following(Matcher matcher) {
        return new FollowCommand(matcher.group(1), matcher.group(2));
    }

    private static CommandEntered wall(Matcher matcher) {
        return new WallCommand(matcher.group(1));
    }
}

record CommandMapping(Pattern pattern, Function<Matcher, CommandEntered> function) {
    Optional<CommandEntered> commandEntered(String input) {
        return Optional.of(pattern.matcher(input))
                .filter(Matcher::find)
                .map(function);
    }
}

