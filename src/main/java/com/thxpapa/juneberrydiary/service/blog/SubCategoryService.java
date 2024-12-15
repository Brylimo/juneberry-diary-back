package com.thxpapa.juneberrydiary.service.blog;

import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.domain.blog.SubCategory;

import java.util.Optional;

public interface SubCategoryService {
    Optional<SubCategory> createDefaultSubCategory(Category category);
}
