package org.openchat.repository;

import org.openchat.domain.Post;
import org.openchat.domain.User;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements PostRepository, UserRepository {

    private List<Post> posts = new ArrayList<>();

    @Override
    public void storePost(Post post) {
        posts.add(post);
    }

    @Override
    public List<Post> retrievePosts() {
        return posts;
    }

    @Override
    public void storeUser(User user) {

    }
}