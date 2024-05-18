package org.socialnetworking.domain;

import java.time.Instant;

public record Posted(String userName, String message, Instant timestamp) {
}
