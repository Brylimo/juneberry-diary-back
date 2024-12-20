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
    private final CategoryService categoryService;

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
    public List<Blog> getAllBlogs(JuneberryUser user) {
        return blogRepository.findBlogsByUser(user);
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

            // 디폴트 카테고리 생성 (카테고리가 아무것도 없을 경우 대비)
            categoryService.createDefaultCategory(createdBlog);

            return createdBlog;
        } else {
            return blog.get();
        }
    }
}
