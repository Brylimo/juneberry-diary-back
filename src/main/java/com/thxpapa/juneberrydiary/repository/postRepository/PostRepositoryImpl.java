package com.thxpapa.juneberrydiary.repository.postRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thxpapa.juneberrydiary.domain.post.Post;
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
    public List<Post> searchTempPost(String blogId, Pageable pageable) {
        List<Post> result = queryFactory
                .selectFrom(post)
                .join(post.blog, blog).fetchJoin()
                .where(blog.blogId.eq(blogId)
                        .and(post.isTemp.eq(true)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return result;
    }
}
