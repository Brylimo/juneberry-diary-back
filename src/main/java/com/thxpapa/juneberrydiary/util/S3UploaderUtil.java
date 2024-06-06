package com.thxpapa.juneberrydiary.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploaderUtil {
    @Value("${aws.s3.bucket}")
    private String bucket;
    @Value("${aws.s3.publicUrl}")
    private String publicUrl;

    private final AmazonS3 amazonS3Client;

    public JuneberryFile uploadFile(MultipartFile multipartFile, String type) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, type);
    }

    private JuneberryFile upload(File uploadFile, String type) {
        String uuid = UUID.randomUUID().toString();
        String ext = getFileExt(uploadFile.getName());

        String fileName = uuid + '.' + ext;
        String path = publicUrl + fileName;

        putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return JuneberryFile.builder()
                .name(uuid)
                .ext(ext)
                .type(type)
                .path(path)
                .build();
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private String getFileExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        String ext = originalFileName.substring(pos + 1);

        return ext;
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
