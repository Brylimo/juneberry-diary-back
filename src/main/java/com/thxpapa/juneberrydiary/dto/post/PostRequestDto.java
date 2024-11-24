package com.thxpapa.juneberrydiary.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public class PostRequestDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WritePost {
        private String postId;
        private String date;
        private String title;
        private String description;
        private String content;
        private String thumbnailPath;
        private Boolean isTemp;
        private Boolean isPublic;
        private String blogId;
        private ArrayList<String> tags;
        private MultipartFile thumbnailImg;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchPostList {
        private String blogId;
        private Boolean isTemp;
        private Boolean isPublic;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchPostByIndex {
        private String blogId;
        private Long index;
    }
}
