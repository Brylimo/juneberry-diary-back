package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    long countByTempPost(String blogId);
    List<Post> searchPostList(PostRequestDto.SearchPostList searchPostList, Pageable pageable);
    Optional<Post> findPostByIndex(PostRequestDto.SearchPostByIndex searchPostByIndex);
}