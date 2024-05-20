package org.socialnetworking.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.socialnetworking.domain.CommandEntered.*;

class CommandEnteredTest {

    @Test
    void shouldRecognisePostCommand() {
        assertThat(CommandParser.commandFor("Alice -> I love the weather today")).isEqualTo(new PostCommand("Alice", "I love the weather today"));
        assertThat(CommandParser.commandFor("Bob -> Damn! We lost!")).isEqualTo(new PostCommand("Bob", "Damn! We lost!"));
        assertThat(CommandParser.commandFor("Bob -> Good game though.")).isEqualTo(new PostCommand("Bob", "Good game though."));
    }

    @Test
    void shouldRecogniseReadCommand() {
       assertThat(CommandParser.commandFor("Alice")).isEqualTo(new ReadCommand("Alice"));
       assertThat(CommandParser.commandFor("Bob")).isEqualTo(new ReadCommand("Bob"));
    }

    @Test
    void shouldRecogniseFollowingCommand() {
        assertThat(CommandParser.commandFor("Charlie follows Alice")).isEqualTo(new FollowCommand("Charlie", "Alice"));
        assertThat(CommandParser.commandFor("Charlie follows Bob")).isEqualTo(new FollowCommand("Charlie", "Bob"));
    }

    @Test
    void shouldRecogniseWallCommand() {
        assertThat(CommandParser.commandFor("Charlie wall")).isEqualTo(new WallCommand("Charlie"));
        assertThat(CommandParser.commandFor("Bob wall")).isEqualTo(new WallCommand( "Bob"));
    }
}