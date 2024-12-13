package com.thxpapa.juneberrydiary.repository.postRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.post.Tag;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.thxpapa.juneberrydiary.domain.blog.QBlog.*;
import static com.thxpapa.juneberrydiary.domain.blog.QCategory.*;
import static com.thxpapa.juneberrydiary.domain.blog.QSubCategory.*;
import static com.thxpapa.juneberrydiary.domain.post.QPost.*;
import static com.thxpapa.juneberrydiary.domain.post.QPostTag.*;
import static com.thxpapa.juneberrydiary.domain.post.QTag.*;

public class TagRepositoryImpl implements TagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public TagRepositoryImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }

    @Override
    public List<Tag> findTagsByPost(Post p) {
        List<Tag> result = queryFactory
                .selectFrom(tag)
                .join(tag.postTags, postTag).fetchJoin().leftJoin(postTag.post, post).fetchJoin()
                .where(post.postUid.eq(p.getPostUid()))
                .fetch();

        return result;
    }

    @Override
    public List<Tag> findTagsByBlog(Blog b) {
        List<Tag> result = queryFactory
                .selectFrom(tag).distinct()
                .join(tag.postTags, postTag).fetchJoin()
                .join(postTag.post, post).fetchJoin()
                .join(post.subCategory, subCategory).fetchJoin()
                .join(subCategory.category, category).fetchJoin()
                .join(category.blog, blog).fetchJoin()
                .where(blog.blogId.eq(b.getBlogId()))
                .fetch();

        return result;
    }
}
