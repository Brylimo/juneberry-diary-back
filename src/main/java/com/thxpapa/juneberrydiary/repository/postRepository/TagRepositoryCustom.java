package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.post.Tag;

import java.util.List;

public interface TagRepositoryCustom {
    List<Tag> findTagsByPost(Post p);
    List<Tag> findTagsByBlog(Blog b);
}
