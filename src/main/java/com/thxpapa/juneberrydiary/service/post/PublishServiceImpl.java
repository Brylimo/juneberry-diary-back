package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.repository.fileRepository.JuneberryFileRepository;
import com.thxpapa.juneberrydiary.util.FileUtil;
import com.thxpapa.juneberrydiary.util.S3UploaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublishServiceImpl implements PublishService {
    private final S3UploaderUtil s3UploaderUtil;
    private final JuneberryFileRepository juneberryFileRepository;

    @Override
    public JuneberryFile uploadImage(JuneberryUser user, MultipartFile multipartFile) {
        try {
            JuneberryFile res = null;

            if (multipartFile != null && !multipartFile.isEmpty() && !Objects.isNull(multipartFile.getOriginalFilename())) {
                JuneberryFile file = s3UploaderUtil.uploadFile(multipartFile, "post");
                res = juneberryFileRepository.save(file);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
