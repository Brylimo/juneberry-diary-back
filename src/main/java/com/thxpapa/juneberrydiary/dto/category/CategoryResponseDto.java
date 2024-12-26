package com.thxpapa.juneberrydiary.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CategoryResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        private String categoryName;
        private Integer count;
        private List<SubCategoryInfo> children;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubCategoryInfo {
        private String subCategoryName;
        private Integer count;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryListInfo {
        private Long total;
        private List<CategoryInfo> categoryInfoList;
    }
}
