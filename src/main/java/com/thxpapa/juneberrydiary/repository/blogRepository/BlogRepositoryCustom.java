package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;

import java.util.List;

public interface BlogRepositoryCustom {
    List<Blog> findBlogsByUser(JuneberryUser user);
}
