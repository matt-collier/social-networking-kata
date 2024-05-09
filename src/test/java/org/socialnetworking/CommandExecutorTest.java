package org.socialnetworking;

import org.junit.jupiter.api.Test;
import org.socialnetworking.domain.CommandEntered;
import org.socialnetworking.domain.Posted;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class CommandExecutorTest {

    private static final Instant NOW = LocalDateTime.of(2024, 5, 4, 11, 12, 20).toInstant(ZoneOffset.UTC);
    private static final Instant FIVE_MINUTES_AGO = LocalDateTime.of(2024, 5, 4, 11, 7, 20).toInstant(ZoneOffset.UTC);
    private static final Instant TWO_SECONDS_AGO = LocalDateTime.of(2024, 5, 4, 11, 12, 18).toInstant(ZoneOffset.UTC);

    private final Repository repository = new Repository();
    private CommandExecutor commandExecutor = new CommandExecutor(repository, () -> NOW);

    @Test
    void shouldPostMessagesToTimeline() {
        final var outputMessage = commandExecutor.execute(new CommandEntered.Post("Alice", "In wonderland again"));
        assertThat(outputMessage.lines()).isEmpty();
        assertThat(repository.fetchTimeline("Alice")).containsExactly("In wonderland again");
    }

    @Test
    void shouldReadTimeline() {
        repository.addToTimeline(new Posted("Bob", "Hi, Bob here", NOW));
        repository.addToTimeline(new Posted("Bob", "Whats going on?", NOW));
        final var outputMessage = commandExecutor.execute(new CommandEntered.Read("Bob"));
        assertThat(outputMessage.lines()).containsExactly("Hi, Bob here", "Whats going on?");
    }

    @Test
    void shouldBeAbleToFollowAndSeeSubscriptionPostsOnWall() {
        repository.addToTimeline(new Posted("Alice", "I love the weather today", FIVE_MINUTES_AGO));
        repository.addToTimeline(new Posted("Charlie", "I'm in New York today! Anyone wants to have a coffee?", TWO_SECONDS_AGO));

        commandExecutor.execute(new CommandEntered.Follows("Charlie", "Alice"));

        final var outputMessage = commandExecutor.execute(new CommandEntered.Wall("Charlie"));
        assertThat(outputMessage.lines()).containsExactly("Charlie - I'm in New York today! Anyone wants to have a coffee? (2 seconds ago)", "Alice - I love the weather today (5 minutes ago)");

    }
}