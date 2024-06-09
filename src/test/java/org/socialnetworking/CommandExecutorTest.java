package org.socialnetworking;

import org.junit.jupiter.api.Test;
import org.socialnetworking.domain.Posted;
import org.socialnetworking.domain.Timeline;
import org.socialnetworking.domain.VoidResponse;
import org.socialnetworking.domain.Wall;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.socialnetworking.domain.CommandEntered.*;

class CommandExecutorTest {

    private static final Instant NOW = LocalDateTime.of(2024, 5, 4, 11, 12, 20).toInstant(ZoneOffset.UTC);
    private static final Instant FIVE_MINUTES_AGO = LocalDateTime.of(2024, 5, 4, 11, 7, 20).toInstant(ZoneOffset.UTC);
    private static final Instant TWO_SECONDS_AGO = LocalDateTime.of(2024, 5, 4, 11, 12, 18).toInstant(ZoneOffset.UTC);

    private final Repository repository = new Repository();
    private final CommandExecutor commandExecutor = new CommandExecutor(repository, () -> NOW);

    @Test
    void shouldPostMessagesToTimeline() {
        final var response = commandExecutor.execute(new PostCommand("Alice", "In wonderland again"));
        assertThat(response).isInstanceOf(VoidResponse.class);
        assertThat(repository.fetchTimeline("Alice").messages()).containsExactly("In wonderland again");
    }

    @Test
    void shouldReadTimeline() {
        repository.addToTimeline(new Posted("Bob", "Hi, Bob here", NOW));
        repository.addToTimeline(new Posted("Bob", "Whats going on?", NOW));
        final var response = commandExecutor.execute(new ReadCommand("Bob"));
        assertThat(response).isInstanceOfSatisfying(Timeline.class, timeline -> {
                    assertThat(timeline.messages()).containsExactly("Hi, Bob here", "Whats going on?");
                });
    }

    @Test
    void shouldBeAbleToFollowAndSeeSubscriptionPostsOnWall() {
        repository.addToTimeline(new Posted("Alice", "I love the weather today", FIVE_MINUTES_AGO));
        repository.addToTimeline(new Posted("Charlie", "I'm in New York today! Anyone wants to have a coffee?", TWO_SECONDS_AGO));

        commandExecutor.execute(new FollowCommand("Charlie", "Alice"));

        final var response = commandExecutor.execute(new WallCommand("Charlie"));
        assertThat(response).isInstanceOfSatisfying(Wall.class, wall -> {
            assertThat(wall.postedList())
                    .containsExactly(new Posted("Charlie",
                            "I'm in New York today! Anyone wants to have a coffee?", TWO_SECONDS_AGO),
                            new Posted("Alice", "I love the weather today", FIVE_MINUTES_AGO));
        });
    }
}