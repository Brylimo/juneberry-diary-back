package com.thxpapa.juneberrydiary.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BlogRequestDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateBlog {
        private String blogId;
        private String blogName;
    }
}
