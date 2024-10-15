package com.thxpapa.juneberrydiary.repository.postRepository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.thxpapa.juneberrydiary.domain.blog.QBlog.*;
import static com.thxpapa.juneberrydiary.domain.post.QPost.*;

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
                .selectFrom(post)
                .join(post.blog, blog).fetchJoin()
                .where(blog.blogId.eq(blogId)
                        .and(post.isTemp.eq(true)))
                .fetchCount();

        return result;
    }

    @Override
    public List<Post> searchPostList(PostRequestDto.SearchPostList searchPostList, Pageable pageable) {
        List<Post> result = queryFactory
                .selectFrom(post)
                .join(post.blog, blog).fetchJoin()
                .where(blog.blogId.eq(searchPostList.getBlogId()), isTempEq(searchPostList.getIsTemp()), isPublicEq(searchPostList.getIsPublic()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.modDt.desc())
                .fetch();

        return result;
    }

    private BooleanExpression isPublicEq(Boolean isPublic) {
        return isPublic != null ? post.isPublic.eq(isPublic) : null;
    }

    private BooleanExpression isTempEq(Boolean isTemp) {
        return isTemp != null ? post.isTemp.eq(isTemp) : null;
    }
}