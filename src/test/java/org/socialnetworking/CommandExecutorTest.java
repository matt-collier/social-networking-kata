package org.socialnetworking;

import org.junit.jupiter.api.Test;
import org.socialnetworking.domain.CommandEntered;
import org.socialnetworking.domain.User;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CommandExecutorTest {

    private final Repository repository = new Repository();
    private final CommandExecutor commandExecutor = new CommandExecutor(repository);

    @Test
    void shouldPostMessagesToTimeline() {
        final var outputMessage = commandExecutor.execute(new CommandEntered.Post("Alice", "In wonderland again"));
        assertThat(outputMessage.lines()).isEmpty();
        assertThat(repository.fetchTimeline("Alice")).containsExactly("In wonderland again");
    }

    @Test
    void shouldReadTimeline() {
        repository.addToTimeline("Bob", "Hi, Bob here");
        repository.addToTimeline("Bob", "Whats going on?");
        final var outputMessage = commandExecutor.execute(new CommandEntered.Read("Bob"));
        assertThat(outputMessage.lines()).containsExactly("Hi, Bob here", "Whats going on?");
    }

    @Test
    void shouldBeAbleToFollowAndSeeSubscriptionPostsOnWall() {
        repository.addToTimeline("Alice", "I love the weather today");
        repository.addToTimeline("Charlie", "I'm in New York today! Anyone wants to have a coffee?");

        commandExecutor.execute(new CommandEntered.Follows("Charlie", "Alice"));

        final var outputMessage = commandExecutor.execute(new CommandEntered.Wall("Charlie"));
        assertThat(outputMessage.lines()).containsExactly("Charlie - I'm in New York today! Anyone wants to have a coffee?", "Alice - I love the weather today");
    }
}