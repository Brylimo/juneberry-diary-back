package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.post.Tag;

public interface PostTagRepositoryCustom {
    void deletePostTag(Post p, Tag t);
}
