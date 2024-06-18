package com.thxpapa.juneberrydiary.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
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
        InputStream inputStream = multipartFile.getInputStream();
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());

        int orientation = 1; // Default orientation

        try {
            // Read the EXIF metadata
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Rotate image based on orientation
        BufferedImage rotatedImage = bi;
        switch (orientation) {
            case 6: // 90 degrees cw
                rotatedImage = Scalr.rotate(bi, Scalr.Rotation.CW_90);
                break;
            case 3: // 180 degrees
                rotatedImage = Scalr.rotate(bi, Scalr.Rotation.CW_180);
                break;
            case 8: // 90 degrees CCW
                rotatedImage = Scalr.rotate(bi, Scalr.Rotation.CW_270);
                break;
        }

        // image compression
        int targetWidth = Math.min(rotatedImage.getWidth(), 2048);
        if (rotatedImage.getWidth() > 2048) {
            rotatedImage = resizeImage(rotatedImage, targetWidth);
        }

        File uploadFile = convert(rotatedImage, multipartFile.getOriginalFilename())
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
        String ext = "jpeg";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        try {
            if (file.createNewFile()) {
                try (OutputStream os = new FileOutputStream(file)) {
                    ImageIO.write(image, ext, os);
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
