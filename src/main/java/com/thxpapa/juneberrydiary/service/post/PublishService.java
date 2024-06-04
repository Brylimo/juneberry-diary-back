package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PublishService {
    JuneberryFile uploadImage(JuneberryUser user, MultipartFile file);
}
