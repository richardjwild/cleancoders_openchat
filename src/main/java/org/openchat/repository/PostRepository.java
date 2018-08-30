package org.openchat.repository;

import org.openchat.domain.Post;

import java.util.List;

public interface PostRepository {
    void storePost(Post post);

    List<Post> retrievePosts();
}
