package org.socialnetworking;

import org.socialnetworking.domain.OutputMessage;
import org.socialnetworking.domain.Posted;

import java.util.*;
import java.util.stream.Stream;

public class Repository {
    private final Map<String, List<Posted>> postMap = new HashMap<>();
    private Map<String, Set<String>> subscriptions = new HashMap<>();

    public void addToTimeline(final String userName, final String message) {
        if(postMap.containsKey(userName)) {
            postMap.get(userName).add(new Posted(userName, message));
        } else {
            postMap.put(userName, new ArrayList<>(List.of(new Posted(userName, message))));
        }
    }

    public List<String> fetchTimeline(final String userName) {
        return userPosts(userName).map(Posted::message).toList();
    }


    public List<Posted> wallFor(final String user) {
        return Stream.concat(userPosts(user),
                             subscriptionPosts(subscriptions.get(user)))
                .toList();
    }

    private Stream<Posted> subscriptionPosts(final Set<String> subscriptions) {
        return subscriptions.stream().flatMap(this::userPosts);
    }

    private Stream<Posted> userPosts(final String userName) {
        return postMap.get(userName).stream();
    }

    public OutputMessage follows(final String subscriber, final String target) {
        if(subscriptions.containsKey(subscriber)) {
            subscriptions.get(subscriber).add(target);
        } else {
            subscriptions.put(subscriber, new HashSet<>(Set.of(target)));
        }
        return new OutputMessage(List.of());
    }
}
