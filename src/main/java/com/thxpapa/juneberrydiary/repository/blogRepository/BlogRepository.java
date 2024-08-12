package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, String> {
}