package com.thxpapa.juneberrydiary.service.blog;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.blog.BlogRequestDto;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    Optional<Blog> getBlogById(String id);
    Blog createBlog(JuneberryUser user, BlogRequestDto.CreateBlog createBlog);
    List<Blog> getAllBlogs(JuneberryUser user);
}