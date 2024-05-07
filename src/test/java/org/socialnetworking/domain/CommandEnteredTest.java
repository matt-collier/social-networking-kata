package org.socialnetworking.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandEnteredTest {

    @Test
    void shouldRecognisePostCommand() {
        assertThat(CommandEntered.parse("Alice -> I love the weather today")).isEqualTo(new CommandEntered.Post("Alice", "I love the weather today"));
        assertThat(CommandEntered.parse("Bob -> Damn! We lost!")).isEqualTo(new CommandEntered.Post("Bob", "Damn! We lost!"));
        assertThat(CommandEntered.parse("Bob -> Good game though.")).isEqualTo(new CommandEntered.Post("Bob", "Good game though."));
    }

    @Test
    void shouldRecogniseReadCommand() {
        assertThat(CommandEntered.parse("Alice")).isEqualTo(new CommandEntered.Read("Alice"));
        assertThat(CommandEntered.parse("Bob")).isEqualTo(new CommandEntered.Read("Bob"));
    }

    @Test
    void shouldRecogniseFollowingCommand() {
        assertThat(CommandEntered.parse("Charlie follows Alice")).isEqualTo(new CommandEntered.Follows("Charlie", "Alice"));
        assertThat(CommandEntered.parse("Charlie follows Bob")).isEqualTo(new CommandEntered.Follows("Charlie", "Bob"));
    }
}