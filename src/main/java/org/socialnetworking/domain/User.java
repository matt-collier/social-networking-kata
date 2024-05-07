package org.socialnetworking.domain;

import java.util.Set;

public final class User {
    private final String userName;
    private final Set<String> subscriptions;

    public User(String userName, Set<String> subscriptions) {
        this.userName = userName;
        this.subscriptions = subscriptions;
    }

    public String userName() {
        return userName;
    }

    public Set<String> subscriptions() {
        return subscriptions;
    }
}
