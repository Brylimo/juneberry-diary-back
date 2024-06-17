package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findPostByJuneberryUserAndPostUid(JuneberryUser user, UUID id);
    List<Post> findPostByJuneberryUserAndIsTemp(JuneberryUser user, boolean isTemp, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.juneberryUser = :user AND p.isTemp = true AND p.postUid = :id")
    Optional<Post> findTempPostByJuneberryUserAndId(@Param("user") JuneberryUser user, @Param("id") UUID id);
    long countByIsTemp(boolean isTemp);
}