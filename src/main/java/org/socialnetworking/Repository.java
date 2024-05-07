package org.socialnetworking;

import org.socialnetworking.domain.Post;
import org.socialnetworking.domain.User;

import java.util.*;
import java.util.stream.Stream;

public class Repository {
    private final Map<String, List<Post>> postMap = new HashMap<>();

    public void addToTimeline(final String userName, final String message) {
        if(postMap.containsKey(userName)) {
            postMap.get(userName).add(new Post(userName, message));
        } else {
            postMap.put(userName, new ArrayList<>(List.of(new Post(userName, message))));
        }
    }

    public List<String> fetchTimeline(final String userName) {
        return userPosts(userName).map(Post::message).toList();
    }


    public List<Post> wallFor(final User user) {
        return Stream.concat(userPosts(user.userName()),
                             subscriptionPosts(user.subscriptions()))
                .toList();
    }

    private Stream<Post> subscriptionPosts(final Set<String> subscriptions) {
        return subscriptions.stream().flatMap(this::userPosts);
    }

    private Stream<Post> userPosts(final String userName) {
        return postMap.get(userName).stream();
    }

}
