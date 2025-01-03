package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.dto.post.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface PostService {
    JuneberryFile uploadImage(String postId, MultipartFile file);
    Post storePost(PostRequestDto.WritePost writePost);
    Post updatePost(PostRequestDto.WritePost writePost);
    Optional<PostResponseDto.PostInfo> getPostById(UUID id);
    Optional<PostResponseDto.PostInfo> getPostByIndex(PostRequestDto.SearchPostByIndex searchPostByIndex);
    Page<Post> getPostList(PostRequestDto.SearchPostList searchPostList, int pageNumber, int pageSize);
    long getPostCnt(String blogId, PostResponseDto.PostInfo postInfo);
    void deletePostById(UUID id);
}
