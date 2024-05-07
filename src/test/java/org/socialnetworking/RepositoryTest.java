package org.socialnetworking;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryTest {

    private final Repository repository = new Repository();


    @Test
    void shouldBeAbleToPublishMessageToTimeline() {
        repository.addToTimeline("Alice", "Hello World");
        assertThat(repository.fetchTimeline("Alice")).contains("Hello World");
    }
    @Test
    void shouldBeAbleToPublishAnotherMessageToTimeline() {
        repository.addToTimeline("Bob", "Something else");
        assertThat(repository.fetchTimeline("Bob")).contains("Something else");
    }


}
