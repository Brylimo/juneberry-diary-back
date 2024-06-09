package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface PublishService {
    JuneberryFile uploadImage(JuneberryUser user, String postId, MultipartFile file);
    Post storePost(JuneberryUser user, PostRequestDto.WritePost writePost);
    Post updatePost(JuneberryUser user, PostRequestDto.WritePost writePost);
    Optional<Post> getTempPostById(JuneberryUser user, UUID id);
}
