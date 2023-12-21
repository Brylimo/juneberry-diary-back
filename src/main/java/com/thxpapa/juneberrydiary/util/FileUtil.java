package com.thxpapa.juneberrydiary.util;

import com.thxpapa.juneberrydiary.domain.file.MerciFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class FileUtil {
    @Value("${file.upload.dir}")
    private String fileDir;

    public String getMerciFilePath(String filename) {
        return fileDir + filename;
    }

    public MerciFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) return null;

        String originalFileName = multipartFile.getOriginalFilename();
        String newFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(getMerciFilePath(newFileName)));

        return MerciFile.builder()
                        .uploadName(originalFileName)
                        .storeName(newFileName)
                        .statusCd("01")
                        .build();
    }

    private String createStoreFileName(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        String ext = originalFileName.substring(pos + 1);

        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

}
