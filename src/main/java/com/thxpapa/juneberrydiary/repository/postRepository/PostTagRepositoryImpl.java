package com.thxpapa.juneberrydiary.repository.postRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.post.QPostTag;
import com.thxpapa.juneberrydiary.domain.post.Tag;
import jakarta.persistence.EntityManager;

import static com.thxpapa.juneberrydiary.domain.post.QPostTag.*;

public class PostTagRepositoryImpl implements PostTagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PostTagRepositoryImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }

    @Override
    public void deletePostTag(Post p, Tag t) {
        long count = queryFactory
                .delete(postTag)
                .where(postTag.post.postUid.eq(p.getPostUid()).and(postTag.tag.tagUid.eq(t.getTagUid())))
                .execute();
    }
}