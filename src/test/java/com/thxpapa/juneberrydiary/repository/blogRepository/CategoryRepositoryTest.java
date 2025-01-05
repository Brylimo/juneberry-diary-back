package com.thxpapa.juneberrydiary.repository.blogRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class CategoryRepositoryTest {
    private final CategoryRepository categoryRepository;

    public CategoryRepositoryTest(
        @Autowired CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    @DisplayName("카테고리 이름과 Blog를 이용해 카테고리를 조회한다.")
    @Test
    void findFirstByNameAndBlog() {
        // given


        // when

        // then
    }
}