package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.dto.category.CategoryPositionDto;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findCategoriesByBlogId(String blogId);
    void updateCategoriesOrder(List<CategoryPositionDto> updateCategoryList);
}
