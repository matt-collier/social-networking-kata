package org.socialnetworking.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandEnteredTest {

    @Test
    void shouldRecognisePostCommand() {
        assertThat(CommandParser.commandFor("Alice -> I love the weather today")).isEqualTo(new CommandEntered.Post("Alice", "I love the weather today"));
        assertThat(CommandParser.commandFor("Bob -> Damn! We lost!")).isEqualTo(new CommandEntered.Post("Bob", "Damn! We lost!"));
        assertThat(CommandParser.commandFor("Bob -> Good game though.")).isEqualTo(new CommandEntered.Post("Bob", "Good game though."));
    }

    @Test
    void shouldRecogniseReadCommand() {
       assertThat(CommandParser.commandFor("Alice")).isEqualTo(new CommandEntered.Read("Alice"));
       assertThat(CommandParser.commandFor("Bob")).isEqualTo(new CommandEntered.Read("Bob"));
    }

    @Test
    void shouldRecogniseFollowingCommand() {
        assertThat(CommandParser.commandFor("Charlie follows Alice")).isEqualTo(new CommandEntered.Follows("Charlie", "Alice"));
        assertThat(CommandParser.commandFor("Charlie follows Bob")).isEqualTo(new CommandEntered.Follows("Charlie", "Bob"));
    }

    @Test
    void shouldRecogniseWallCommand() {
        assertThat(CommandParser.commandFor("Charlie wall")).isEqualTo(new CommandEntered.Wall("Charlie"));
        assertThat(CommandParser.commandFor("Bob wall")).isEqualTo(new CommandEntered.Wall( "Bob"));
    }
}