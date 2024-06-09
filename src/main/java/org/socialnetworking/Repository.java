package org.socialnetworking;

import org.socialnetworking.domain.Posted;
import org.socialnetworking.domain.Timeline;
import org.socialnetworking.domain.Wall;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class Repository {
    private final Map<String, List<Posted>> postMap = new HashMap<>();
    private final Map<String, Set<String>> subscriptions = new HashMap<>();

    public void addToTimeline(Posted posted) {
        if(postMap.containsKey(posted.userName())) {
            postMap.get(posted.userName()).add(posted);
        } else {
            postMap.put(posted.userName(), new ArrayList<>(List.of(posted)));
        }
    }

    public Timeline fetchTimeline(String userName) {
        return new Timeline(userPosts(userName).map(Posted::message).toList());
    }


    public Wall wallFor(String user) {
        return new Wall(Stream.concat(userPosts(user),
                             subscriptionPosts(subscriptions.get(user)))
                .sorted(comparing(Posted::timestamp).reversed())
                .toList());
    }

    private Stream<Posted> subscriptionPosts(Set<String> subscriptions) {
        return subscriptions.stream().flatMap(this::userPosts);
    }

    private Stream<Posted> userPosts(String userName) {
        return postMap.get(userName).stream();
    }

    public void follows(String subscriber, final String target) {
        if(subscriptions.containsKey(subscriber)) {
            subscriptions.get(subscriber).add(target);
        } else {
            subscriptions.put(subscriber, new HashSet<>(Set.of(target)));
        }
    }
}
