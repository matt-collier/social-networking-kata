package org.socialnetworking.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.socialnetworking.domain.CommandEntered.*;

public sealed interface CommandEntered permits Follows, Post, Read, Wall {

    Pattern POST = Pattern.compile("(.*) -> (.*)");
    Pattern READ = Pattern.compile("^\\s*(\\S+)\\s*$");
    Pattern FOLLOWING = Pattern.compile("(.*) follows (.*)");
    Pattern WALL = Pattern.compile("(.*) wall");


    static CommandEntered parse(String input) {
        final Matcher postMatcher = POST.matcher(input);
        if (postMatcher.find()) {
            return new Post(postMatcher.group(1), postMatcher.group(2));
        }
        final Matcher readMatcher = READ.matcher(input);
        if (readMatcher.find()) {
            return new Read(readMatcher.group(0));
        }
        final Matcher followerMatcher = FOLLOWING.matcher(input);
        if (followerMatcher.find()) {
            return new Follows(followerMatcher.group(1), followerMatcher.group(2));
        }
        final Matcher wallMatcher = WALL.matcher(input);
        if (wallMatcher.find()) {
            return new Wall(wallMatcher.group(1));
        }
        throw new UnsupportedOperationException("Command %s not supported".formatted(input));
    }

    record Post(String userName, String message) implements CommandEntered {
    }

    record Read(String userName) implements CommandEntered {
    }

    record Follows(String subscriber, String target) implements CommandEntered {
    }

    record Wall(String user) implements CommandEntered {
    }
}
