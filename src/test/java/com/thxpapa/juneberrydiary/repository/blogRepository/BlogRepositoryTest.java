package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.blog.BlogUser;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.repository.userRepository.JuneberryUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class BlogRepositoryTest {
    private final BlogRepository blogRepository;
    private final BlogUserRepository blogUserRepository;
    private final JuneberryUserRepository juneberryUserRepository;

    public BlogRepositoryTest(
            @Autowired BlogRepository blogRepository,
            @Autowired BlogUserRepository blogUserRepository,
            @Autowired JuneberryUserRepository juneberryUserRepository
    ) {
        this.blogRepository = blogRepository;
        this.blogUserRepository = blogUserRepository;
        this.juneberryUserRepository = juneberryUserRepository;
    }

    @DisplayName("유저와 연관된 모든 블로그들을 조회한다.")
    @Test
    void findBlogsByUser() {
        // given
        JuneberryUser user1 = juneberryUserRepository.save(createUser("김준베", "juneberry@jbd.com", "juneberry", "juneberrypassword12"));
        JuneberryUser user2 = juneberryUserRepository.save(createUser("김철수", "soo@jbd.com", "soo12", "soosoopassword12"));

        Blog blog1 = blogRepository.save(createBlog("blogId1", "first"));
        Blog blog2 = blogRepository.save(createBlog("blogId2", "second"));
        Blog blog3 = blogRepository.save(createBlog("blogId3", "third"));
        Blog blog4 = blogRepository.save(createBlog("blogId4", "fourth"));

        blogUserRepository.save(createBlogUser(user1, blog1));
        blogUserRepository.save(createBlogUser(user1, blog2));
        blogUserRepository.save(createBlogUser(user2, blog3));
        blogUserRepository.save(createBlogUser(user1, blog4));

        // when
        List<Blog> blogList = blogRepository.findBlogsByUser(user1);

        // then
        assertThat(blogList).hasSize(3)
                .extracting("blogId", "blogName")
                .containsExactlyInAnyOrder(
                        tuple("blogId1", "first"),
                        tuple("blogId2", "second"),
                        tuple("blogId4", "fourth")
                );
    }

    private JuneberryUser createUser(String name, String email, String username, String password) {
        return JuneberryUser.builder()
                .name(name)
                .email(email)
                .username(username)
                .password(password)
                .build();
    }

    private Blog createBlog(String blogId, String blogName) {
        return Blog.builder()
                .blogId(blogId)
                .blogName(blogName)
                .build();
    }

    private BlogUser createBlogUser(JuneberryUser user, Blog blog) {
        return BlogUser.builder()
                .juneberryUser(user)
                .blog(blog)
                .build();
    }
}