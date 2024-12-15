package com.thxpapa.juneberrydiary.service.blog;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.dto.category.CategoryRequestDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> createDefaultCategory(Blog blog);
    void storeCategories(String blogId, CategoryRequestDto.CreateCategory createCategoryDto);
    List<Category> getCategoryList(String blogId);
}
