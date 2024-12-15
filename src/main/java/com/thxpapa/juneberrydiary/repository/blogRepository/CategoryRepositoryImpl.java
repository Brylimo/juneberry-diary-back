package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thxpapa.juneberrydiary.domain.blog.Category;

import jakarta.persistence.EntityManager;

import java.util.List;

import static com.thxpapa.juneberrydiary.domain.blog.QBlog.*;
import static com.thxpapa.juneberrydiary.domain.blog.QCategory.*;
import static com.thxpapa.juneberrydiary.domain.blog.QSubCategory.*;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * sub 카테고리와 카테고리 모두 가져오기
     */
    @Override
    public List<Category> findCategoriesByBlogId(String blogId) {
        List<Category> result = queryFactory
                .selectFrom(category)
                .join(category.blog, blog).fetchJoin()
                .leftJoin(category.subCategories, subCategory).fetchJoin()
                .where(blog.blogId.eq(blogId))
                .fetch();

        return result;
    }
}
