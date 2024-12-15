package com.thxpapa.juneberrydiary.web.v1;

import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.category.CategoryRequestDto;
import com.thxpapa.juneberrydiary.dto.category.CategoryResponseDto;
import com.thxpapa.juneberrydiary.service.blog.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/blog/{blogId}/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final ResponseDto responseDto;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategories(@PathVariable("blogId") String blogId, @RequestBody CategoryRequestDto.CreateCategory createCategoryDto) {
        try {
            categoryService.storeCategories(blogId, createCategoryDto);

            return responseDto.success();
        } catch (Exception e) {
            log.debug("addCategorise error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCategories(@PathVariable("blogId") String blogId) {
        try {
            List<Category> categoryList = categoryService.getCategoryList(blogId);

            List<CategoryResponseDto.CategoryInfo> categoryInfoList = categoryList.stream()
                    .map(category -> CategoryResponseDto.CategoryInfo.builder()
                            .categoryName(category.getName())
                            .children(category.getSubCategories().stream()
                                    .map(subCategory -> CategoryResponseDto.SubCategoryInfo.builder()
                                            .subCategoryName(subCategory.getName())
                                            .build())
                                    .collect(Collectors.toList())) // subCategory 리스트로 변환
                            .build())
                    .collect(Collectors.toList());

            return responseDto.success(categoryInfoList);
        } catch (Exception e) {
            log.debug("getAllCategories error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
