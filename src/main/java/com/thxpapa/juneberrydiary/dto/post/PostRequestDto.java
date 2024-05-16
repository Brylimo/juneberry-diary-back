package com.thxpapa.juneberrydiary.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class PostRequestDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditorImgData {
        private MultipartFile editorImg;
    }
}
