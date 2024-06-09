package org.socialnetworking.console;

import org.socialnetworking.domain.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ConsoleView {

    public OutputMessage outputFor(Response response, Instant currentTime) {
        return switch (response) {
            case VoidResponse ignored -> new OutputMessage(List.of());
            case Wall wall -> wallView(wall, currentTime);
            case Timeline timeline -> new OutputMessage(timeline.messages());
        };
    }

    private OutputMessage wallView(Wall wall, final Instant currentTime) {
        final List<String> list = wall.postedList().stream()
                .map(post -> "%s - %s (%s)".formatted(post.userName(), post.message(), howLongAgoPosted(post, currentTime)))
                .toList();
        return new OutputMessage(list);
    }

    private String howLongAgoPosted(Posted posted, Instant currentTime) {
        final var difference  = Duration.between(posted.timestamp(), currentTime);

        return switch (difference) {
            case Duration d when d.toDays() > 0 -> d.toDays() + " days ago";
            case Duration d when d.toHours() > 0 -> d.toHours() + " hours ago";
            //todo - handle cases where day, hour and seconds are one so text should use the singular
            case Duration d when d.toMinutes() == 1 -> "1 minute ago";
            case Duration d when d.toMinutes() > 1 -> d.toMinutes() + " minutes ago";
            default ->  difference.toSeconds() + " seconds ago";
        };
    }
}
