package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;

import java.util.Optional;

public interface PublishService {
    void uploadImage(JuneberryUser user, PostRequestDto.EditorImgData postEditorImageRequestDto);
}
