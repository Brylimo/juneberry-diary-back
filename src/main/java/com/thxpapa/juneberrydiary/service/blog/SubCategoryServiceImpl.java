package com.thxpapa.juneberrydiary.service.blog;

import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.domain.blog.SubCategory;
import com.thxpapa.juneberrydiary.repository.blogRepository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;

    @Override
    @Transactional
    public Optional<SubCategory> createDefaultSubCategory(Category category) {
        if (category != null) {
            SubCategory defaultSubCategory = subCategoryRepository.save(SubCategory.builder()
                            .name("")
                            .category(category)
                            .build());

            return Optional.ofNullable(defaultSubCategory);
        }
        return Optional.empty();
    }
}
