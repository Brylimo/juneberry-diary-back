package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.thxpapa.juneberrydiary.domain.blog.QBlog.*;
import static com.thxpapa.juneberrydiary.domain.blog.QBlogUser.*;
import static com.thxpapa.juneberrydiary.domain.user.QJuneberryUser.*;

public class BlogRepositoryImpl implements BlogRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BlogRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 다대다 연결 엔티티인 Blog를 꺼내와 반환
     */
    @Override
    public List<Blog> findBlogsByUser(JuneberryUser user) {
        List<Blog> result = queryFactory
                .selectFrom(blog)
                .leftJoin(blog.blogUsers, blogUser).fetchJoin().leftJoin(blogUser.juneberryUser, juneberryUser).fetchJoin()
                .where(juneberryUser.juneberryUserUid.eq(user.getJuneberryUserUid()))
                .fetch();

        return result;
    }

    /**
     * user id 조건으로 JuneberryUser를 조회하기 위한 동적 쿼리 메서드
     */
    private BooleanExpression juneberryUserUidEq(Long juneberryUserUid) {
        return juneberryUserUid == null ? null : juneberryUser.juneberryUserUid.eq(juneberryUserUid);
    }
}