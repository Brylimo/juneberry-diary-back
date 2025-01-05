package com.thxpapa.juneberrydiary.repository.postRepository;

import com.thxpapa.juneberrydiary.domain.post.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class TagRepositoryTest {
    private final TagRepository tagRepository;

    public TagRepositoryTest(
            @Autowired TagRepository tagRepository
    ) {
        this.tagRepository = tagRepository;
    }

    @DisplayName("태그 이름에 맞는 태그를 조회한다.")
    @Test
    void findTagsByName() {
        // given
        Tag tag1 = createTag("준베리다이어리");
        Tag tag2 = createTag("여행");

        tagRepository.saveAll(List.of(tag1, tag2));
        // when
        Tag tag = tagRepository.findTagByName("여행").get();

        // then
        assertThat(tag.getName()).isEqualTo("여행");
    }

    @DisplayName("Post를 이용해 태그 목록을 조회한다.")
    @Test
    void findTagsByPost() { // todo 다대다 관계 테스트 코드 작성
        // given

        // when

        // then
    }

    @DisplayName("Blog를 이용해 태그 목록을 조회한다.")
    @Test
    void findTagsByBlog() { // todo 다대다 관계 테스트 코드 작성
        // given

        // when

        // then
    }

    private Tag createTag(String name) {
        return Tag.builder()
                .name(name)
                .build();
    }
}