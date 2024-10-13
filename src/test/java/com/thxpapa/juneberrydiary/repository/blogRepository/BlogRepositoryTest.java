package com.thxpapa.juneberrydiary.repository.blogRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class BlogRepositoryTest {
    @Autowired
    private BlogRepository blogRepository;

    @DisplayName("유저와 연관된 모든 블로그들을 조회한다.")
    @Test
    void findBlogsByUser() {
        // given

        // when

        // then
    }
}