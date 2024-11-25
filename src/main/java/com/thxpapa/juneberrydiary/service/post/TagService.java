package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.post.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags(String blogId);
}
