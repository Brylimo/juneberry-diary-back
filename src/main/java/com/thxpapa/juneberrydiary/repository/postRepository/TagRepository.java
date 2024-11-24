package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    Optional<Tag> findTagByName(String name);
}
