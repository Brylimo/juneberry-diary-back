package com.thxpapa.juneberrydiary.dto.category;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class CategoryRequestDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCategory {
        private List<CategoryResponseDto.CategoryInfo> categoryInfos;
    }
}
