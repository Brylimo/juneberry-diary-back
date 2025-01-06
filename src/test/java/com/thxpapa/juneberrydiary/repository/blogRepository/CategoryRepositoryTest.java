package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.dto.category.CategoryPositionDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class CategoryRepositoryTest {
    private final CategoryRepository categoryRepository;
    private final BlogRepository blogRepository;

    public CategoryRepositoryTest(
        @Autowired CategoryRepository categoryRepository,
        @Autowired BlogRepository blogRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.blogRepository = blogRepository;
    }

    @DisplayName("카테고리 이름과 Blog를 이용해 카테고리를 조회한다.")
    @Test
    void findFirstByNameAndBlog() {
        // given
        Blog blog1 = blogRepository.save(createBlog("blogId1", "first"));
        categoryRepository.save(createCategory("PS", 0, blog1));
        categoryRepository.save(createCategory("Language", 2, blog1));

        // when
        Blog blog = blogRepository.findById("blogId1").get();
        Category category = categoryRepository.findFirstByNameAndBlog("Language", blog).get();

        // then
        assertThat(category)
                .extracting("name", "position")
                .contains("Language", 2);
    }

    @DisplayName("블로그 아이디를 이용해 카테고리 목록을 조회한다.")
    @Test
    void findCategoriesByBlogId() {
        // given
        Blog blog1 = blogRepository.save(createBlog("blogId1", "first"));
        categoryRepository.save(createCategory("PS", 0, blog1));
        categoryRepository.save(createCategory("Language", 2, blog1));
        categoryRepository.save(createCategory("Code Review", 1, blog1));

        // when
        List<Category> categoryList = categoryRepository.findCategoriesByBlogId("blogId1");

        // then
        assertThat(categoryList).hasSize(3)
                .extracting("name", "position")
                .containsExactlyInAnyOrder(
                        tuple("PS", 0),
                        tuple("Language", 2),
                        tuple("Code Review", 1)
                );
    }

    @DisplayName("카테고리의 순서를 변경한다.")
    @Transactional
    @Test
    void updateCategoriesOrder() {
        // todo 같은 position일 경우 순서가 뒤바뀌는 상황 발생

        // given
        Blog blog1 = blogRepository.save(createBlog("blogId1", "first"));
        Category category1 = categoryRepository.save(createCategory("PS", 0, blog1));
        Category category2 = categoryRepository.save(createCategory("Code Review", 1, blog1));
        Category category3 = categoryRepository.save(createCategory("Language", 2, blog1));
        Category category4 = categoryRepository.save(createCategory("Trip", 3, blog1));

        List<CategoryPositionDto> updateCategoryList = new ArrayList<>();
        updateCategoryList.add(CategoryPositionDto.builder()
                .categoryUid(category1.getCategoryUid())
                .position(3)
                .build());
        updateCategoryList.add(CategoryPositionDto.builder()
                .categoryUid(category2.getCategoryUid())
                .position(2)
                .build());
        updateCategoryList.add(CategoryPositionDto.builder()
                .categoryUid(category3.getCategoryUid())
                .position(1)
                .build());
        updateCategoryList.add(CategoryPositionDto.builder()
                .categoryUid(category4.getCategoryUid())
                .position(0)
                .build());

        // when
        categoryRepository.updateCategoriesOrder(updateCategoryList);

        // then
        List<Category> categoryList = categoryRepository.findCategoriesByBlogId("blogId1");
        assertThat(categoryList).hasSize(4)
                .extracting("name")
                .containsExactly(
                        "Trip",
                        "Language",
                        "Code Review",
                        "PS"
                );
    }

    private Blog createBlog(String blogId, String blogName) {
        return Blog.builder()
                .blogId(blogId)
                .blogName(blogName)
                .build();
    }

    private Category createCategory(String name, int position, Blog blog) {
        return Category.builder()
                .name(name)
                .position(position)
                .blog(blog)
                .build();
    }
}