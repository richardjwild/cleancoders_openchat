package org.openchat.domain;

import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;
import org.openchat.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;

public class PostService {

    private Clock clock;
    private PostIdGenerator postIdGenerator;
    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(
            Clock clock,
            PostIdGenerator postIdGenerator,
            PostRepository postRepository,
            UserService userService)
    {
        this.clock = clock;
        this.postIdGenerator = postIdGenerator;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<Post> timelineFor(String userId) {
        return postRepository.retrievePosts().stream()
                .filter(post -> post.isByUser(userId))
                .sorted(comparing(Post::dateTime).reversed())
                .collect(Collectors.toList());
    }

    public Post createPost(String userId, String text) {
        LocalDateTime dateTime = clock.now();
        String postId = postIdGenerator.nextId();
        Post post = new Post(userId, text, dateTime, postId);
        postRepository.storePost(post);
        return post;
    }

    public Collection<Post> wallPosts(String followerId) {
        return userService.findUser(followerId)
                .map(follower -> followerPlusTheirFollowees(follower)
                        .map(User::id)
                        .map(this::timelineFor)
                        .flatMap(Collection::stream)
                        .sorted(comparing(Post::dateTime).reversed())
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    private Stream<User> followerPlusTheirFollowees(User follower) {
        return Stream.concat(Stream.of(follower), follower.usersFollowing().stream());
    }
}