package org.socialnetworking.console;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.socialnetworking.domain.OutputMessage;
import org.socialnetworking.domain.Posted;
import org.socialnetworking.domain.Wall;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleViewTest {
    //todo - test that inexact time differences work as expected (and should days, hours etc get rounded up or down?)
    private static final Instant NOW = LocalDateTime.of(2024, 5, 4, 11, 12, 20).toInstant(ZoneOffset.UTC);

    private static final Instant TWO_DAYS_AGO = LocalDateTime.of(2024, 5, 2, 11, 12, 20).toInstant(ZoneOffset.UTC);
    private static final Instant ONE_DAY_AGO = LocalDateTime.of(2024, 5, 3, 11, 12, 20).toInstant(ZoneOffset.UTC);
    private static final Instant TWO_HOURS_AGO = LocalDateTime.of(2024, 5, 4, 9, 12, 20).toInstant(ZoneOffset.UTC);
    private static final Instant ONE_HOUR_AGO = LocalDateTime.of(2024, 5, 4, 10, 12, 20).toInstant(ZoneOffset.UTC);
    private static final Instant TWO_MINUTES_AGO = LocalDateTime.of(2024, 5, 4, 11, 10, 20).toInstant(ZoneOffset.UTC);
    private static final Instant ONE_MINUTE_AGO = LocalDateTime.of(2024, 5, 4, 11, 11, 20).toInstant(ZoneOffset.UTC);
    private static final Instant TWO_SECONDS_AGO = LocalDateTime.of(2024, 5, 4, 11, 12, 18).toInstant(ZoneOffset.UTC);
    private static final Instant ONE_SECOND_AGO = LocalDateTime.of(2024, 5, 4, 11, 12, 19).toInstant(ZoneOffset.UTC);

    public static Stream<Arguments> providePostedMessages() {
        return Stream.of(
                Arguments.of(new Posted("Alice", "Hello World!", TWO_DAYS_AGO), "Alice - Hello World! (2 days ago)"),
                Arguments.of(new Posted("Charlie", "Something else.", ONE_DAY_AGO), "Charlie - Something else. (1 day ago)"),
                Arguments.of(new Posted("Bob", "Hey. Bob here", TWO_HOURS_AGO), "Bob - Hey. Bob here (2 hours ago)"),
                Arguments.of(new Posted("Alice", "Hello World!", ONE_HOUR_AGO), "Alice - Hello World! (1 hour ago)"),
                Arguments.of(new Posted("Charlie", "Well, well, well", TWO_MINUTES_AGO), "Charlie - Well, well, well (2 minutes ago)"),
                Arguments.of(new Posted("Bob", "What about Bob", ONE_MINUTE_AGO), "Bob - What about Bob (1 minute ago)"),
                Arguments.of(new Posted("Alice", "Go ask Alice, When she's ten feet tall", TWO_SECONDS_AGO), "Alice - Go ask Alice, When she's ten feet tall (2 seconds ago)"),
                Arguments.of(new Posted("Alice", "Remember what the dormouse said", ONE_SECOND_AGO), "Alice - Remember what the dormouse said (1 second ago)")
                );
    }


    @ParameterizedTest
    @MethodSource("providePostedMessages")
    void shouldGiveWallOutputMessages(Posted posted, String expectedOutput) {
        ConsoleView consoleView = new ConsoleView();
        OutputMessage outputMessage = consoleView.outputFor(new Wall(List.of(posted)), NOW);
        assertThat(outputMessage.lines()).containsExactly(expectedOutput);
    }
}