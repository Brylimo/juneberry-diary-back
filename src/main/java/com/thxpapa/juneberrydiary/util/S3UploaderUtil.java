package com.thxpapa.juneberrydiary.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());

        // image compression
        int targetWidth = Math.min(bi.getWidth(), 2048);

        if (bi.getWidth() > 2048) {
            bi = resizeImage(bi, targetWidth);
        }

        File uploadFile = convert(bi, multipartFile.getOriginalFilename())
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

    private Optional<File> convert(BufferedImage image, String originalFilename) {
        File file = new File(originalFilename);
        try {
            if (file.createNewFile()) {
                try (OutputStream os = new FileOutputStream(file)) {
                    ImageIO.write(image, "jpg", os);
                }
                return Optional.of(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) throws IOException {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, targetWidth, Scalr.OP_ANTIALIAS);
    }
}
