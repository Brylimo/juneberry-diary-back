package com.thxpapa.juneberrydiary.repository.blogRepository;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.domain.blog.SubCategory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class SubCategoryRepositoryTest {
    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryRepositoryTest(
            @Autowired BlogRepository blogRepository,
            @Autowired CategoryRepository categoryRepository,
            @Autowired SubCategoryRepository subCategoryRepository
    ) {
        this.blogRepository = blogRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @DisplayName("서브 카테고리 이름과 카테고리를 이용해 서브 카테고리를 조회한다.")
    @Test
    void findFirstByNameAndCategory() {
        // given
        Blog blog = blogRepository.save(createBlog("blogId1", "first"));
        Category category = categoryRepository.save(createCategory("PS", 0, blog));

        subCategoryRepository.save(createSubCategory("one", 0, category));
        subCategoryRepository.save(createSubCategory("two", 1, category));
        subCategoryRepository.save(createSubCategory("three", 2, category));
        subCategoryRepository.save(createSubCategory("four", 3, category));

        // when
        SubCategory subCategory = subCategoryRepository.findFirstByNameAndCategory("one", category).get();

        // then
        assertThat(subCategory)
                .extracting("name", "position")
                .contains("one", 0);
    }

    @DisplayName("서브 카테고리 리스트에 포함된 서브 카테고리를 모두 지운다.")
    @Transactional
    @Test
    void deleteSubCategories() {
        // given
        Blog blog = blogRepository.save(createBlog("blogId1", "first"));
        Category category = categoryRepository.save(createCategory("PS", 0, blog));

        SubCategory subCategory1 = subCategoryRepository.save(createSubCategory("one", 0, category));
        SubCategory subCategory2 = subCategoryRepository.save(createSubCategory("two", 1, category));
        SubCategory subCategory3 = subCategoryRepository.save(createSubCategory("three", 2, category));
        SubCategory subCategory4 = subCategoryRepository.save(createSubCategory("four", 3, category));

        List<SubCategory> allSubCategories = subCategoryRepository.findAll();
        assertThat(allSubCategories).hasSize(4);

        // when
        ArrayList<SubCategory> subCategories = new ArrayList<>(Arrays.asList(subCategory1, subCategory2));
        subCategoryRepository.deleteSubCategories(subCategories); // delete

        // then
        List<SubCategory> remainingSubCategories = subCategoryRepository.findAll();
        assertThat(remainingSubCategories).hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder("three", "four");

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
    private SubCategory createSubCategory(String name, int position, Category category) {
        return SubCategory.builder()
                .name(name)
                .position(position)
                .category(category)
                .build();
    }
}