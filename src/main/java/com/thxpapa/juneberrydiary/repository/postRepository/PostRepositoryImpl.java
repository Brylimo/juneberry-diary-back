package com.thxpapa.juneberrydiary.repository.postRepository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.thxpapa.juneberrydiary.domain.blog.QBlog.*;
import static com.thxpapa.juneberrydiary.domain.blog.QCategory.*;
import static com.thxpapa.juneberrydiary.domain.blog.QSubCategory.*;
import static com.thxpapa.juneberrydiary.domain.file.QJuneberryFile.*;
import static com.thxpapa.juneberrydiary.domain.post.QPost.*;
import static com.thxpapa.juneberrydiary.domain.post.QPostTag.*;
import static com.thxpapa.juneberrydiary.domain.post.QTag.*;

public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * BlogId에 속해 있는 모든 일시저장 포스트를 가져오기
     */
    @Override
    public long countByTempPost(String blogId) {
        long result = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.subCategory, subCategory)
                .leftJoin(subCategory.category, category)
                .join(category.blog, blog)
                .where(blog.blogId.eq(blogId)
                        .and(post.isTemp.eq(true)))
                .fetchOne();

        return result;
    }

    @Override
    public Page<Post> searchPostList(PostRequestDto.SearchPostList searchPostList, Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post).distinct()
                .leftJoin(post.subCategory, subCategory).fetchJoin()
                .leftJoin(subCategory.category, category).fetchJoin()
                .join(category.blog, blog).fetchJoin()
                .leftJoin(post.juneberryFile, juneberryFile).fetchJoin()
                .leftJoin(post.postTags, postTag).fetchJoin().leftJoin(postTag.tag, tag).fetchJoin()
                .where(blog.blogId.eq(searchPostList.getBlogId()), isTempEq(searchPostList.getIsTemp()), isPublicEq(searchPostList.getIsPublic()), selectCategoryIdsWithCategory(searchPostList.getCategory(), searchPostList.getSubCategory()), selectPostIdsWithTag(searchPostList.getTagName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.regDt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.subCategory, subCategory)
                .leftJoin(subCategory.category, category)
                .join(category.blog, blog)
                .leftJoin(post.postTags, postTag)
                .leftJoin(postTag.tag, tag)
                .where(blog.blogId.eq(searchPostList.getBlogId()), isTempEq(searchPostList.getIsTemp()), isPublicEq(searchPostList.getIsPublic()), selectCategoryIdsWithCategory(searchPostList.getCategory(), searchPostList.getSubCategory()), selectPostIdsWithTag(searchPostList.getTagName()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    /**
     * index를 통해 post를 가져오기
     */
    @Override
    public Optional<Post> findPostByIndex(PostRequestDto.SearchPostByIndex searchPostByIndex) {
        Post result = queryFactory
                .selectFrom(post)
                .leftJoin(post.subCategory, subCategory).fetchJoin()
                .leftJoin(subCategory.category, category).fetchJoin()
                .join(category.blog, blog).fetchJoin()
                .where(blog.blogId.eq(searchPostByIndex.getBlogId()), post.index.eq(searchPostByIndex.getIndex()))
                .fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Post> findPostByPostUid(UUID id) {
        Post result = queryFactory
                .selectFrom(post)
                .leftJoin(post.subCategory, subCategory).fetchJoin()
                .leftJoin(subCategory.category, category).fetchJoin()
                .join(category.blog, blog).fetchJoin()
                .where(post.postUid.eq(id))
                .fetchFirst();

        return Optional.ofNullable(result);
    }

    private BooleanExpression selectCategoryIdsWithCategory(String category, String subCategory) {
        if (category != null && !category.isEmpty() && subCategory != null && !subCategory.isEmpty()) {
            // category와 subCategory가 모두 존재하는 경우
            return post.subCategory.category.name.eq(category)
                    .and(post.subCategory.name.eq(subCategory));
        } else if (category != null && !category.isEmpty()) {
            // category만 존재하고 subCategory는 없는 경우
            return post.subCategory.category.name.eq(category)
                    .and(post.subCategory.name.isNull());
        }

        return Expressions.TRUE;
    }

    private BooleanExpression selectPostIdsWithTag(String tagName) {
        return tagName != null ? post.postUid.in(JPAExpressions.select(postTag.post.postUid)
                .from(postTag)
                .join(postTag.tag, tag)
                .where(tag.name.eq(tagName))
                .distinct()) : null;
    }

    private BooleanExpression isPublicEq(Boolean isPublic) {
        return isPublic != null ? post.isPublic.eq(isPublic) : null;
    }

    private BooleanExpression isTempEq(Boolean isTemp) {
        return isTemp != null ? post.isTemp.eq(isTemp) : null;
    }
}