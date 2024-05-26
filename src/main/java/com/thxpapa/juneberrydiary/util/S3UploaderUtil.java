package com.thxpapa.juneberrydiary.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploaderUtil {
    @Value("${aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public JuneberryFile storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty() || Objects.isNull(multipartFile.getOriginalFilename())) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        return null;
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
