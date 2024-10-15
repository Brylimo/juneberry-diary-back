package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublishService {
    JuneberryFile uploadImage(String blogId, String postId, MultipartFile file);
    Post storePost(PostRequestDto.WritePost writePost);
    Post updatePost(PostRequestDto.WritePost writePost);
    Optional<Post> getPostById(String blogId, UUID id);
    List<Post> getPostList(PostRequestDto.SearchPostList searchPostList, int pageNumber, int pageSize);
    long getTempPostCnt(String blogId);
}
