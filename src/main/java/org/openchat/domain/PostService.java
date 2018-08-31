package org.openchat.domain;

import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;
import org.openchat.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostService {

    private Clock clock;
    private PostIdGenerator postIdGenerator;
    private final PostRepository postRepository;

    public PostService(Clock clock, PostIdGenerator postIdGenerator, PostRepository postRepository) {
        this.clock = clock;
        this.postIdGenerator = postIdGenerator;
        this.postRepository = postRepository;
    }

    public List<Post> timelineFor(String userId) {
        return postRepository.retrievePosts().stream()
                .filter(post -> post.isByUser(userId))
                .collect(Collectors.toList());
    }

    public Post createPost(String userId, String text) {
        LocalDateTime dateTime = clock.now();
        String postId = postIdGenerator.nextId();
        Post post = new Post(userId, text, dateTime, postId);
        postRepository.storePost(post);
        return post;
    }
}