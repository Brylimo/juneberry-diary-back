package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
