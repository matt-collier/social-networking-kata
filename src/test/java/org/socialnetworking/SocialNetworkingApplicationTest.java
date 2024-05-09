package org.socialnetworking;

import org.junit.jupiter.api.Test;
import org.socialnetworking.console.ApplicationRunner;
import org.socialnetworking.console.ConsoleReader;
import org.socialnetworking.console.ConsoleWriter;
import org.socialnetworking.domain.CommandEntered;
import org.socialnetworking.domain.Posted;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class SocialNetworkingApplicationTest {
    private static final Instant NOW = LocalDateTime.of(2024, 5, 4, 11, 12, 20).toInstant(ZoneOffset.UTC);
    private static final Instant FIVE_MINUTES_AGO = LocalDateTime.of(2024, 5, 4, 11, 7, 20).toInstant(ZoneOffset.UTC);
    private static final Instant TWO_SECONDS_AGO = LocalDateTime.of(2024, 5, 4, 11, 12, 18).toInstant(ZoneOffset.UTC);

    private final Repository repository = new Repository();
    private CommandExecutor commandExecutor = new CommandExecutor(repository, () -> NOW);

    @Test
    void shouldBeAbleToPostMessages() throws IOException {
        var input = """
                Alice -> I love the weather today
                Alice -> Its a beautiful summers day
                Alice
                Q
                """;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(output);
             ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes())) {

            new ApplicationRunner(new ConsoleReader(inputStream), new ConsoleWriter(printStream), commandExecutor).runApplication();

            assertThat(output.toString()).contains("I love the weather today", "Its a beautiful summers day");
        }
    }

    @Test
    void shouldBeAbleToReadUsersTimeline() throws IOException {
        commandExecutor.execute(new CommandEntered.Post("Alice", "Well, what is going on?"));
        commandExecutor.execute(new CommandEntered.Post("Bob", "Some interesting things have happened"));
        commandExecutor.execute(new CommandEntered.Post("Alice", "Hello, is it me your looking for"));
        commandExecutor.execute(new CommandEntered.Post("Bob", "Eh?"));

        var input = """
                Bob
                Q
                """;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(output);
             ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes())) {

            new ApplicationRunner(new ConsoleReader(inputStream), new ConsoleWriter(printStream), commandExecutor).runApplication();

            assertThat(output.toString().split(System.lineSeparator())).containsExactly(
        "Some interesting things have happened", "Eh?");
        }
    }

    @Test
    void shouldBeAbleToFollowOtherUsersAndDisplayTheirPostsOnWall() throws IOException {

        repository.addToTimeline(new Posted("Alice", "I love the weather today", FIVE_MINUTES_AGO));
        repository.addToTimeline(new Posted("Charlie", "I'm in New York today! Anyone wants to have a coffee?", TWO_SECONDS_AGO));

        var input = """
                Charlie follows Alice
                Charlie wall
                Q
                """;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(output);
             ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes())) {

            new ApplicationRunner(new ConsoleReader(inputStream), new ConsoleWriter(printStream), commandExecutor).runApplication();

            assertThat(output.toString().split(System.lineSeparator())).containsExactly(
                    "Charlie - I'm in New York today! Anyone wants to have a coffee? (2 seconds ago)", "Alice - I love the weather today (5 minutes ago)");
        }
    }
}