package org.socialnetworking.domain;

public record Posted(String userName, String message, java.time.Instant timestamp) {
}
