package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.thxpapa.juneberrydiary.domain.blog.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findCategoriesByBlogId(String blogId);
}
