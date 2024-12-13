package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PostRepositoryCustom {
    long countByTempPost(String blogId);
    Page<Post> searchPostList(PostRequestDto.SearchPostList searchPostList, Pageable pageable);
    Optional<Post> findPostByPostUid(UUID id);
    Optional<Post> findPostByIndex(PostRequestDto.SearchPostByIndex searchPostByIndex);
}
