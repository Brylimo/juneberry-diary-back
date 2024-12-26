package com.thxpapa.juneberrydiary.web.v1;

import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.category.CategoryRequestDto;
import com.thxpapa.juneberrydiary.dto.category.CategoryResponseDto;
import com.thxpapa.juneberrydiary.dto.post.PostResponseDto;
import com.thxpapa.juneberrydiary.service.blog.CategoryService;
import com.thxpapa.juneberrydiary.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    private final PostService postService;
    private final ResponseDto responseDto;

    @Operation(summary = "카테고리 저장", description = "전체 카테고리 조직도를 받아 저장합니다.")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategories(
            @Parameter(required = true, description = "블로그 아이디")
            @PathVariable("blogId") String blogId,
            @Parameter(description = "카테고리 조직도")
            @RequestBody CategoryRequestDto.CreateCategory createCategoryDto) {
        try {
            categoryService.storeCategories(blogId, createCategoryDto);

            return responseDto.success();
        } catch (Exception e) {
            log.debug("addCategorise error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "카테고리 목록 조회", description = "블로그에 해당하는 모든 카테고리 목록을 조회합니다.")
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCategories(
            @Parameter(required = true, description = "블로그 아이디")
            @PathVariable("blogId") String blogId) {
        try {
            List<Category> categoryList = categoryService.getCategoryList(blogId);

            PostResponseDto.PostInfo postInfo = PostResponseDto.PostInfo.builder()
                    .isPublic(true)
                    .isTemp(false)
                    .build();

            long total = postService.getPostCnt(blogId, postInfo);
            List<CategoryResponseDto.CategoryInfo> categoryInfoList = categoryList.stream()
                    .map(category -> CategoryResponseDto.CategoryInfo.builder()
                            .categoryName(category.getName())
                            .count(category.getSubCategories().stream()
                                    .filter(subCategory -> subCategory.getName().equals(""))
                                    .findFirst() // Optional<SubCategory> 반환
                                    .map(subCategory -> subCategory.getPosts().size())
                                    .orElse(0))
                            .children(category.getSubCategories().stream().distinct()
                                    .map(subCategory -> CategoryResponseDto.SubCategoryInfo.builder()
                                            .subCategoryName(subCategory.getName())
                                            .count(subCategory.getPosts().size())
                                            .build())
                                    .collect(Collectors.toList())) // subCategory 리스트로 변환
                            .build())
                    .collect(Collectors.toList());

            return responseDto.success(CategoryResponseDto.CategoryListInfo.builder()
                            .categoryInfoList(categoryInfoList)
                            .total(total)
                            .build());
        } catch (Exception e) {
            log.debug("getAllCategories error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
