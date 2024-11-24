package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.PostTag;
import com.thxpapa.juneberrydiary.domain.post.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long>, PostTagRepositoryCustom {
    long countByTag(Tag tag);
}
