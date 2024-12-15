package com.thxpapa.juneberrydiary.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageInfo {
        private String imagePath;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostInfo {
        private String id;
        private Long index;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd. HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime registeredDateTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd. HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime updatedDateTime;
        private String category;
        private String subCategory;
        private String title;
        private String description;
        private String content;
        private List<String> tags;
        private Boolean isTemp;
        private Boolean isPublic;
        private String thumbnailPath;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostListInfo {
        private Long totalCount;
        private List<PostResponseDto.PostInfo> postInfoList;
    }
}
