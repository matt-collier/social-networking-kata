package org.socialnetworking;

import org.junit.jupiter.api.Test;
import org.socialnetworking.console.ApplicationRunner;
import org.socialnetworking.console.ConsoleReader;
import org.socialnetworking.console.ConsoleWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class SocialNetworkingApplicationTest {

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

            new ApplicationRunner(new ConsoleReader(inputStream), new ConsoleWriter(printStream), new Repository()).runApplication();

            assertThat(output.toString()).contains("I love the weather today", "Its a beautiful summers day");
        }
    }

    @Test
    void shouldBeAbleToReadUsersTimeline() throws IOException {
        final Repository repository = new Repository();
        repository.addToTimeline("Alice", "Well, what is going on?");
        repository.addToTimeline("Bob", "Some interesting things have happened");
        repository.addToTimeline("Alice", "Hello, is it me your looking for");
        repository.addToTimeline("Bob", "Eh?");

        var input = """
                Bob
                Q
                """;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(output);
             ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes())) {

            new ApplicationRunner(new ConsoleReader(inputStream), new ConsoleWriter(printStream), repository).runApplication();

            assertThat(output.toString().split(System.lineSeparator())).containsExactly(
        "Some interesting things have happened", "Eh?");
        }
    }
}