package org.openchat.repository;

import org.openchat.domain.Post;
import org.openchat.domain.User;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRepository implements PostRepository, UserRepository {

    private Map<String, Post> posts = new HashMap<>();
    private Map<String, User> users = new HashMap<>();

    @Override
    public void storePost(Post post) {
        posts.put(post.postId(), post);
    }

    @Override
    public List<Post> retrievePosts() {
        return entriesFrom(posts);
    }

    @Override
    public void storeUser(User user) {
        users.put(user.id(), user);
    }

    @Override
    public void updateUser(User userToUpdate) {
        storeUser(userToUpdate);
    }

    @Override
    public List<User> retrieveUsers() {
        return entriesFrom(users);
    }

    private <T> List<T> entriesFrom(Map<String, T> map) {
        return map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
}