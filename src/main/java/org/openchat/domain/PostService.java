package org.openchat.domain;

import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;

import java.util.ArrayList;
import java.util.List;

public class PostService {

    private Clock clock;
    private PostIdGenerator postIdGenerator;

    private List<Post> posts = new ArrayList<>();

    public PostService(Clock clock, PostIdGenerator postIdGenerator) {
        this.clock = clock;
        this.postIdGenerator = postIdGenerator;
    }

    public List<Post> retrievePosts() {
        return posts;
    }

    public Post createPost(String userId, String text) {
        Post post = new Post(
                userId,
                text,
                clock.now(),
                postIdGenerator.nextId());
        posts.add(post);
        return post;
    }
}