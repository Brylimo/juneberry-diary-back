package com.thxpapa.juneberrydiary.service.blog;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.blog.BlogUser;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.blog.BlogRequestDto;
import com.thxpapa.juneberrydiary.repository.blogRepository.BlogRepository;
import com.thxpapa.juneberrydiary.repository.blogRepository.BlogUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final BlogUserRepository blogUserRepository;

    @Override
    @Transactional
    public Optional<Blog> getBlogById(String blogId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        return optionalBlog;
    }

    @Override
    @Transactional
    public List<Blog> getAllBlogsByUser(JuneberryUser user) {
        return null;
    }

    @Override
    @Transactional
    public Blog createBlog(JuneberryUser user, BlogRequestDto.CreateBlog createBlog) {
        Optional<Blog> blog = getBlogById(createBlog.getBlogId());

        if (blog.isEmpty() && createBlog.getBlogId() != null) {
            Blog createdBlog = blogRepository.save(Blog.builder()
                    .blogId(createBlog.getBlogId())
                    .blogName(createBlog.getBlogName())
                    .build());

            blogUserRepository.save(BlogUser.builder()
                    .blog(createdBlog)
                    .juneberryUser(user)
                    .build());

            return createdBlog;
        } else {
            return blog.get();
        }
    }
}
