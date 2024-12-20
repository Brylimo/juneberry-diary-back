package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.blog.Category;
import com.thxpapa.juneberrydiary.domain.blog.SubCategory;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.post.PostFile;
import com.thxpapa.juneberrydiary.domain.post.PostTag;
import com.thxpapa.juneberrydiary.domain.post.Tag;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.dto.post.PostResponseDto;
import com.thxpapa.juneberrydiary.repository.blogRepository.BlogRepository;
import com.thxpapa.juneberrydiary.repository.blogRepository.CategoryRepository;
import com.thxpapa.juneberrydiary.repository.blogRepository.SubCategoryRepository;
import com.thxpapa.juneberrydiary.repository.fileRepository.JuneberryFileRepository;
import com.thxpapa.juneberrydiary.repository.postRepository.PostFileRepository;
import com.thxpapa.juneberrydiary.repository.postRepository.PostRepository;
import com.thxpapa.juneberrydiary.repository.postRepository.PostTagRepository;
import com.thxpapa.juneberrydiary.repository.postRepository.TagRepository;
import com.thxpapa.juneberrydiary.util.S3UploaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final S3UploaderUtil s3UploaderUtil;
    private final JuneberryFileRepository juneberryFileRepository;
    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @Override
    @Transactional
    public JuneberryFile uploadImage(String postId, MultipartFile multipartFile) {
        try {
            JuneberryFile res = null;

            UUID id = UUID.fromString(postId);
            Post post = postRepository.findPostByPostUid(id).orElseGet(null);

            if (multipartFile != null &&
                    post != null &&
                    !multipartFile.isEmpty() &&
                    !Objects.isNull(multipartFile.getOriginalFilename())) {
                JuneberryFile file = s3UploaderUtil.uploadFile(multipartFile, "post");
                res = juneberryFileRepository.save(file);
                postFileRepository.save(PostFile.builder()
                                .juneberryFile(file)
                                .post(post)
                                .build());
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public Post storePost(PostRequestDto.WritePost writePost) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(writePost.getDate(), formatter);
            MultipartFile thumbnail = writePost.getThumbnailImg();

            // 썸네일 저장
            JuneberryFile resFile = null;
            if (thumbnail != null) {
                JuneberryFile file = s3UploaderUtil.uploadFile(thumbnail, "post-thumbnail");
                resFile = juneberryFileRepository.save(file);
            }

            // 블로그 fetch
            Blog blog = blogRepository.findById(writePost.getBlogId())
                    .orElseThrow(() -> new NullPointerException("cannot find blog!"));

            // 카테고리 fetch
            Category category = categoryRepository.findFirstByNameAndBlog(writePost.getCategory(), blog)
                    .orElseGet(() -> categoryRepository.save(Category.builder()
                                    .name(writePost.getCategory())
                                    .position(0)
                                    .blog(blog)
                                    .build()));

            // 서브 카테고리 fetch
            SubCategory subCategory = subCategoryRepository.findFirstByNameAndCategory(writePost.getSubCategory(), category)
                    .orElseGet(() -> subCategoryRepository.save(SubCategory.builder()
                                    .name(writePost.getSubCategory())
                                    .category(category)
                                    .build()));

            Long nextPostIdx = null;
            if (!writePost.getIsTemp()) { // 발행되는 포스트 -> index 저장
                nextPostIdx = blog.getPostIdxCnt() + 1;

                // 블로그 postIdxCnt 1 증가
                blog.updatePostIdxCnt(nextPostIdx);
            }

            // 포스트 저장
            Post createdPost = postRepository.save(Post.builder()
                    .title(writePost.getTitle())
                    .content(writePost.getContent())
                    .description(writePost.getDescription())
                    .subCategory(subCategory)
                    .isTemp(writePost.getIsTemp())
                    .isPublic(writePost.getIsPublic())
                    .index(nextPostIdx)
                    .date(date)
                    .juneberryFile(resFile)
                    .build());

            // 태그 저장
            if (writePost.getTags() != null) {
                for (String tag : writePost.getTags()) {
                    Optional<Tag> optionalTag = tagRepository.findTagByName(tag);

                    Tag resTag = optionalTag.orElseGet(
                            () -> tagRepository.save(Tag.builder()
                                    .name(tag)
                                    .build()));

                    postTagRepository.save(PostTag.builder()
                            .tag(resTag)
                            .post(createdPost)
                            .build());
                }
            }

            return createdPost;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public Post updatePost(PostRequestDto.WritePost writePost) {
        try {
            UUID id = UUID.fromString(writePost.getPostId());
            MultipartFile thumbnail = writePost.getThumbnailImg();

            Post post = postRepository.findPostByPostUid(id).orElseGet(() -> storePost(writePost));
            Blog blog = post.getSubCategory().getCategory().getBlog();

            if (!blog.getBlogId().equals(writePost.getBlogId())) {
                throw new Exception("blogId doesn't match");
            }

            if (post.getJuneberryFile() != null && !post.getJuneberryFile().getPath().equals(writePost.getThumbnailPath())) { // thumbnail이 존재하면 삭제
                s3UploaderUtil.deleteFile(post.getJuneberryFile().getPath());
                juneberryFileRepository.deleteById(post.getJuneberryFile().getJuneberryFileUid());
            }

            // 썸네일 저장
            JuneberryFile resFile = null;
            if (thumbnail != null) {
                JuneberryFile file = s3UploaderUtil.uploadFile(thumbnail, "post-thumbnail");
                resFile = juneberryFileRepository.save(file);
            }

            // index 처리
            if (post.getIndex() == null && !writePost.getIsTemp()) {
                Long nextPostIdx = blog.getPostIdxCnt() + 1;

                post.updateIndex(nextPostIdx);
                blog.updatePostIdxCnt(nextPostIdx);
            }

            // 카테고리 처리
            Category orgCategory = post.getSubCategory().getCategory();
            SubCategory orgSubCategory = post.getSubCategory();

            if (!orgCategory.getName().equals(writePost.getCategory())) { // 카테고리가 다를 경우
                Category newCategory = categoryRepository.findFirstByNameAndBlog(writePost.getCategory(), blog)
                        .orElseGet(() -> categoryRepository.save(Category.builder()
                                .name(writePost.getCategory())
                                .position(0)
                                .blog(blog)
                                .build()));

                SubCategory newSubCategory = subCategoryRepository.findFirstByNameAndCategory(writePost.getSubCategory(), newCategory)
                        .orElseGet(() -> subCategoryRepository.save(SubCategory.builder()
                                .name(writePost.getSubCategory())
                                .category(newCategory)
                                .build()));

                post.updateSubCategory(newSubCategory);
            } else if (!orgSubCategory.getName().equals(writePost.getSubCategory())) { // 카테고리는 같으나 서브 카테고리가 다를 경우
                SubCategory newSubCategory = subCategoryRepository.findFirstByNameAndCategory(writePost.getSubCategory(), orgCategory)
                        .orElseGet(() -> subCategoryRepository.save(SubCategory.builder()
                                .name(writePost.getSubCategory())
                                .category(orgCategory)
                                .build()));

                post.updateSubCategory(newSubCategory);
            }

            // tag 처리
            List<Tag> tagList = tagRepository.findTagsByPost(post);

            HashMap<String, Tag> tagMap = new HashMap<>();
            for (Tag tag : tagList) {
                tagMap.put(tag.getName(), tag);
            }

            Set<String> orgSet = new HashSet<>(tagList.stream().map(tag -> tag.getName()).collect(Collectors.toList())); // 기존 존재하던 tag들
            Set<String> newSet = new HashSet<>(Optional.ofNullable(writePost.getTags()).orElse(new ArrayList<>())); // 새로 입력된 tag들

            // tag 삭제
            Set<String> deleteSet = orgSet.stream()
                            .filter(element -> !newSet.contains(element))
                            .collect(Collectors.toSet());

            for (String element : deleteSet) {
                Tag deleteTag = tagMap.get(element);

                postTagRepository.deletePostTag(post, deleteTag);

                long aliveTagCnt = postTagRepository.countByTag(deleteTag);
                if (aliveTagCnt == 0L) { // 사용되는 태그가 존재하지 않으면 삭제
                    tagRepository.deleteById(deleteTag.getTagUid());
                }
            }

            // tag 추가
            Set<String> addSet = newSet.stream()
                    .filter(element -> !orgSet.contains(element))
                    .collect(Collectors.toSet());

            for (String element : addSet) {
                Optional<Tag> optionalTag = tagRepository.findTagByName(element);

                Tag resTag = optionalTag.orElseGet(
                        () -> tagRepository.save(Tag.builder()
                                .name(element)
                                .build()));

                postTagRepository.save(PostTag.builder()
                        .tag(resTag)
                        .post(post)
                        .build());
            }

            post.updatePostByWritePost(writePost, resFile);

            return post;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public Optional<PostResponseDto.PostInfo> getPostById(UUID id) {
        Optional<Post> optionalPost = postRepository.findPostByPostUid(id);

        List<Tag> tagList = new ArrayList<>();
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            tagList = tagRepository.findTagsByPost(post);
        } else {
            // post가 없을 경우 처리
            throw new NoSuchElementException("Post not found");
        }

        if (optionalPost.isEmpty()) {
            return Optional.empty();
        } else {
            Post foundPost = optionalPost.get();
            String thumbnailPath = null;
            if (foundPost.getJuneberryFile() != null) {
                thumbnailPath = foundPost.getJuneberryFile().getPath();
            }

            Optional<PostResponseDto.PostInfo> optionalPostInfo = Optional.ofNullable(PostResponseDto.PostInfo.builder()
                    .id(foundPost.getPostUid().toString())
                    .category(foundPost.getSubCategory().getCategory().getName())
                    .subCategory(foundPost.getSubCategory().getName())
                    .title(foundPost.getTitle())
                    .index(foundPost.getIndex())
                    .description(foundPost.getDescription())
                    .content(foundPost.getContent())
                    .isTemp(foundPost.getIsTemp())
                    .isPublic(foundPost.getIsPublic())
                    .registeredDateTime(foundPost.getRegDt())
                    .updatedDateTime(foundPost.getModDt())
                    .thumbnailPath(thumbnailPath)
                    .tags(tagList.stream()
                            .map(tag -> tag.getName()).collect(Collectors.toList()))
                    .build());

            return optionalPostInfo;
        }
    }

    @Override
    @Transactional
    public Optional<PostResponseDto.PostInfo> getPostByIndex(PostRequestDto.SearchPostByIndex searchPostByIndex) {
        Optional<Post> optionalPost = postRepository.findPostByIndex(searchPostByIndex);

        List<Tag> tagList = new ArrayList<>();
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            tagList = tagRepository.findTagsByPost(post);
        } else {
            // post가 없을 경우 처리
            throw new NoSuchElementException("Post not found");
        }

        if (optionalPost.isEmpty()) {
            return Optional.empty();
        } else {
            Post foundPost = optionalPost.get();
            String thumbnailPath = null;
            if (foundPost.getJuneberryFile() != null) {
                thumbnailPath = foundPost.getJuneberryFile().getPath();
            }

            Optional<PostResponseDto.PostInfo> optionalPostInfo = Optional.ofNullable(PostResponseDto.PostInfo.builder()
                    .id(foundPost.getPostUid().toString())
                    .category(foundPost.getSubCategory().getCategory().getName())
                    .subCategory(foundPost.getSubCategory().getName())
                    .title(foundPost.getTitle())
                    .index(foundPost.getIndex())
                    .description(foundPost.getDescription())
                    .content(foundPost.getContent())
                    .isTemp(foundPost.getIsTemp())
                    .isPublic(foundPost.getIsPublic())
                    .registeredDateTime(foundPost.getRegDt())
                    .updatedDateTime(foundPost.getModDt())
                    .thumbnailPath(thumbnailPath)
                    .tags(tagList.stream()
                            .map(tag -> tag.getName()).collect(Collectors.toList()))
                    .build());

            return optionalPostInfo;
        }
    }

    @Override
    @Transactional
    public Page<Post> getPostList(PostRequestDto.SearchPostList searchPostList, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("regDt").descending());
        return postRepository.searchPostList(searchPostList, pageable);
    }

    @Override
    @Transactional
    public long getTempPostCnt(String blogId) {
        return postRepository.countByTempPost(blogId);
    }

    @Override
    @Transactional
    public void deletePostById(UUID id) {
        Optional<Post> optionalPost = postRepository.findPostByPostUid(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // 태그 삭제
            List<Tag> tagList = tagRepository.findTagsByPost(post);
            for (Tag deleteTag : tagList) {
                postTagRepository.deletePostTag(post, deleteTag);

                long aliveTagCnt = postTagRepository.countByTag(deleteTag);
                if (aliveTagCnt == 0L) {
                    tagRepository.deleteById(deleteTag.getTagUid());
                }
            }

            postRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Post not found");
        }
    }
}