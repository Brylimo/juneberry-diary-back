package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    long countByTempPost(String blogId);
    List<Post> searchTempPost(String blogId, Pageable pageable);
}
