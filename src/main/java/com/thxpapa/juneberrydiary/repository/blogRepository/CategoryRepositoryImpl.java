package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thxpapa.juneberrydiary.domain.blog.Category;

import com.thxpapa.juneberrydiary.dto.category.CategoryPositionDto;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static com.thxpapa.juneberrydiary.domain.blog.QBlog.*;
import static com.thxpapa.juneberrydiary.domain.blog.QCategory.*;
import static com.thxpapa.juneberrydiary.domain.blog.QSubCategory.*;
import static com.thxpapa.juneberrydiary.domain.post.QPost.post;

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
                .orderBy(category.position.asc())
                .fetch();

        return result;
    }

    /**
     * 리스트에 있는 카테고리의 position 모두 한번에 바꾸기
     */
    @Override
    public void updateCategoriesOrder(List<CategoryPositionDto> updateCategoryList) {
        StringBuilder caseWhenClause = new StringBuilder("CASE ");
        List<Long> categoryUids = new ArrayList<>();

        // 업데이트할 categoryUid와 position을 동적으로 추가
        for (int i = 0; i < updateCategoryList.size(); i++) {
            CategoryPositionDto updateCategory = updateCategoryList.get(i);
            caseWhenClause.append("WHEN categoryUid = " + updateCategory.getCategoryUid() + " THEN " + updateCategory.getPosition() + " ");
            categoryUids.add(updateCategory.getCategoryUid());
        }
        caseWhenClause.append("END");

        queryFactory.update(category)
                .set(category.position, Expressions.numberTemplate(Integer.class, caseWhenClause.toString()))
                .where(category.categoryUid.in(categoryUids))
                .execute();
    }
}
