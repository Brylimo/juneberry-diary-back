package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.post.Tag;
import com.thxpapa.juneberrydiary.repository.postRepository.TagRepository;
import com.thxpapa.juneberrydiary.service.blog.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final BlogService blogService;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public List<Tag> getAllTags(String blogId) {
        try {
            Optional<Blog> optionalBlog = blogService.getBlogById(blogId);

            if (optionalBlog.isEmpty()) {
                return new ArrayList<>();
            } else {
                Blog blog = optionalBlog.get();

                List<Tag> tags = tagRepository.findTagsByBlog(blog);
                return tags;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
