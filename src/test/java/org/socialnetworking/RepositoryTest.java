package org.socialnetworking;


import org.junit.jupiter.api.Test;
import org.socialnetworking.domain.Posted;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryTest {
    private static final Instant NOW = LocalDateTime.of(2024, 5, 4, 11, 12).toInstant(ZoneOffset.UTC);

    private final Repository repository = new Repository();


    @Test
    void shouldBeAbleToPublishMessageToTimeline() {
        repository.addToTimeline(new Posted("Alice", "Hello World", NOW));
        assertThat(repository.fetchTimeline("Alice")).contains("Hello World");
    }

    @Test
    void shouldBeAbleToPublishAnotherMessageToTimeline() {
        repository.addToTimeline(new Posted("Bob", "Something else", NOW));
        assertThat(repository.fetchTimeline("Bob")).contains("Something else");
    }


}
