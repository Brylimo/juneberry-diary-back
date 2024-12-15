package com.thxpapa.juneberrydiary.service.blog;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.domain.blog.SubCategory;
import com.thxpapa.juneberrydiary.dto.category.CategoryRequestDto;
import com.thxpapa.juneberrydiary.dto.category.CategoryResponseDto;
import com.thxpapa.juneberrydiary.repository.blogRepository.BlogRepository;
import com.thxpapa.juneberrydiary.repository.blogRepository.CategoryRepository;
import com.thxpapa.juneberrydiary.repository.blogRepository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final SubCategoryService subCategoryService;

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    @Transactional
    public Optional<Category> createDefaultCategory(Blog blog) { // 디폴트 카테고리 생성
        if (blog != null) {
            Category defaultCategory = categoryRepository.save(Category.builder()
                    .name("")
                    .blog(blog)
                    .build());

            subCategoryService.createDefaultSubCategory(defaultCategory);
            return Optional.ofNullable(defaultCategory);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void storeCategories(String blogId, CategoryRequestDto.CreateCategory createCategoryDto) {
        List<Category> categoryList = new ArrayList<>();

        Set<SubCategory> deleteSet = new HashSet<>();
        Set<SubCategory> addSet = new HashSet<>();

        // 블로그 fetch
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new NullPointerException("cannot find blog!"));

        // 이전에 존재했던 카테고리 리스트
        List<Category> oldCategoryList = getCategoryList(blogId);
        Map<String, Category> oldCategoryMap = oldCategoryList.stream()
                .collect(Collectors.toMap(
                        Category::getName,
                        category -> category,
                        (existing, replacement) -> existing,
                        HashMap::new // HashMap 생성
                ));

        for (CategoryResponseDto.CategoryInfo categoryInfo : createCategoryDto.getCategoryInfos()) {
            Category category = Category.builder()
                    .name(categoryInfo.getCategoryName())
                    .blog(blog)
                    .build();

            List<SubCategory> subCategories = new ArrayList<>();

            if (!oldCategoryMap.containsKey(category.getName())) {
                subCategories = categoryInfo.getChildren().stream()
                        .map(subCategoryInfo -> SubCategory.builder()
                                .name(subCategoryInfo.getSubCategoryName())
                                .category(category)
                                .build())
                        .collect(Collectors.toList());

                // default category 추가
                subCategories.add(SubCategory.builder()
                                .name("")
                                .category(category)
                                .build());

                category.updateSubCategories(subCategories);
                categoryList.add(category);

                continue;
            } else {
                subCategories = categoryInfo.getChildren().stream()
                        .map(subCategoryInfo -> SubCategory.builder()
                                .name(subCategoryInfo.getSubCategoryName())
                                .category(oldCategoryMap.get(category.getName()))
                                .build())
                        .collect(Collectors.toList());
            }

            // subcategory 수정
            Set<SubCategory> oldSet = new HashSet<>(oldCategoryMap.containsKey(category.getName()) ? oldCategoryMap.get(category.getName()).getSubCategories() : new ArrayList<>());
            Set<SubCategory> newSet = new HashSet<>(Optional.ofNullable(subCategories).orElse(new ArrayList<>()));

            // subcategory 삭제
            deleteSet.addAll(oldSet.stream()
                    .filter(subCategory -> !newSet.contains(subCategory))
                    .collect(Collectors.toSet()));

            // subcategory 추가
            addSet.addAll(newSet.stream()
                    .filter(subCategory -> !oldSet.contains(subCategory))
                    .collect(Collectors.toSet()));
        }

        if (!categoryList.isEmpty()) {
            categoryRepository.saveAll(categoryList);
        }

        // subcategory 삭제
        if (!deleteSet.isEmpty()) {
            subCategoryRepository.deleteSubCategories(new ArrayList<>(deleteSet));
        }

        // subcategory 추가
        if (!addSet.isEmpty()) {
            subCategoryRepository.saveAll(new ArrayList<>(addSet));
        }
    }

    @Override
    @Transactional
    public List<Category> getCategoryList(String blogId) {
        return categoryRepository.findCategoriesByBlogId(blogId);
    }
}
