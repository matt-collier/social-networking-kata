package org.socialnetworking;

import org.junit.jupiter.api.Test;
import org.socialnetworking.console.ApplicationRunner;
import org.socialnetworking.console.ConsoleReader;
import org.socialnetworking.console.ConsoleWriter;
import org.socialnetworking.domain.Posted;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.socialnetworking.domain.CommandEntered.*;

class ConsoleApplicationTest {
    private static final Instant NOW = LocalDateTime.of(2024, 5, 4, 11, 12, 20).toInstant(ZoneOffset.UTC);
    private static final Instant FIVE_MINUTES_AGO = LocalDateTime.of(2024, 5, 4, 11, 7, 20).toInstant(ZoneOffset.UTC);
    private static final Instant TWO_MINUTES_AGO = LocalDateTime.of(2024, 5, 4, 11, 10, 20).toInstant(ZoneOffset.UTC);
    private static final Instant ONE_MINUTE_AGO = LocalDateTime.of(2024, 5, 4, 11, 11, 20).toInstant(ZoneOffset.UTC);
    private static final Instant TWO_SECONDS_AGO = LocalDateTime.of(2024, 5, 4, 11, 12, 18).toInstant(ZoneOffset.UTC);
    private static final Instant FIFTEEN_SECONDS_AGO = LocalDateTime.of(2024, 5, 4, 11, 12, 5).toInstant(ZoneOffset.UTC);

    private final Repository repository = new Repository();
    private final CommandExecutor commandExecutor = new CommandExecutor(repository, () -> NOW);

    private String input;

    @Test
    void shouldBeAbleToPostMessagesAndReadThemInUsersTimeline() throws IOException {
        givenInput("""
                Alice -> I love the weather today
                Alice -> Its a beautiful summers day
                Alice
                Q
                """);

        whenInputProcessed(output -> assertThat(output).contains("I love the weather today", "Its a beautiful summers day"));
    }

    @Test
    void shouldBeAbleToReadUsersTimeline() throws IOException {
        commandExecutor.execute(new PostCommand("Alice", "Well, what is going on?"));
        commandExecutor.execute(new PostCommand("Bob", "Some interesting things have happened"));
        commandExecutor.execute(new PostCommand("Alice", "Hello, is it me your looking for"));
        commandExecutor.execute(new PostCommand("Bob", "Eh?"));

        givenInput("""
                Bob
                Q
                """);

        whenInputProcessed(output -> assertThat(output.split(System.lineSeparator())).containsExactly(
                "Some interesting things have happened", "Eh?"));
    }

    @Test
    void shouldBeAbleToFollowOtherUsersAndDisplayTheirPostsOnWall() throws IOException {
        repository.addToTimeline(new Posted("Alice", "I love the weather today", FIVE_MINUTES_AGO));
        repository.addToTimeline(new Posted("Charlie", "I'm in New York today! Anyone wants to have a coffee?", TWO_SECONDS_AGO));

        givenInput("""
                Charlie follows Alice
                Charlie wall
                Q
                """);
        whenInputProcessed(output -> assertThat(output.split(System.lineSeparator())).containsExactly(
                "Charlie - I'm in New York today! Anyone wants to have a coffee? (2 seconds ago)", "Alice - I love the weather today (5 minutes ago)"));
    }

    @Test
    void shouldBeAbleToFollowOtherUsersAndDisplayTheirPostsOnWall2() throws IOException {
        repository.follows("Charlie", "Alice");
        repository.addToTimeline(new Posted("Alice", "I love the weather today", FIVE_MINUTES_AGO));
        repository.addToTimeline(new Posted("Bob", "Damn! We lost!", TWO_MINUTES_AGO));
        repository.addToTimeline(new Posted("Bob", "Good game though.", ONE_MINUTE_AGO));
        repository.addToTimeline(new Posted("Charlie", "I'm in New York today! Anyone wants to have a coffee?", FIFTEEN_SECONDS_AGO));
        repository.follows("Charlie", "Bob");


        givenInput("""
                Charlie wall
                Q
                """);
        whenInputProcessed(output -> assertThat(output.split(System.lineSeparator())).containsExactly(
                "Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)",
                "Bob - Good game though. (1 minute ago)",
                "Bob - Damn! We lost! (2 minutes ago)",
                "Alice - I love the weather today (5 minutes ago)"));
    }

    private void givenInput(final String input) {
        this.input = input;
    }

    private void whenInputProcessed(Consumer<String> assertion) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(output);
             ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes())) {

            var applicationRunner = new ApplicationRunner(new ConsoleReader(inputStream), new ConsoleWriter(printStream), commandExecutor, () -> NOW);

            applicationRunner.runApplication();

            assertion.accept(output.toString());
        }
    }
}