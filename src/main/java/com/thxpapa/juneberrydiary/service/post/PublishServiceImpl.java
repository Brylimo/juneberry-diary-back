package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.repository.fileRepository.JuneberryFileRepository;
import com.thxpapa.juneberrydiary.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublishServiceImpl implements PublishService {
    private final FileUtil fileUtil;
    private final JuneberryFileRepository juneberryFileRepository;

    @Override
    public void uploadImage(JuneberryUser user, PostRequestDto.EditorImgData postEditorImageRequestDto) {
        try {
            JuneberryFile file = fileUtil.storeFile(postEditorImageRequestDto.getEditorImg());
            juneberryFileRepository.save(file);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
