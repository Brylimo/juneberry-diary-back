package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.post.PostFile;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.repository.blogRepository.BlogRepository;
import com.thxpapa.juneberrydiary.repository.fileRepository.JuneberryFileRepository;
import com.thxpapa.juneberrydiary.repository.postRepository.PostFileRepository;
import com.thxpapa.juneberrydiary.repository.postRepository.PostRepository;
import com.thxpapa.juneberrydiary.util.S3UploaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final S3UploaderUtil s3UploaderUtil;
    private final JuneberryFileRepository juneberryFileRepository;
    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final BlogRepository blogRepository;

    @Override
    @Transactional
    public JuneberryFile uploadImage(String blogId, String postId, MultipartFile multipartFile) {
        try {
            JuneberryFile res = null;

            UUID id = UUID.fromString(postId);
            Post post = postRepository.findFirstByPostUid(id).orElseGet(null);

            if (multipartFile != null &&
                    post != null &&
                    !multipartFile.isEmpty() &&
                    !Objects.isNull(multipartFile.getOriginalFilename()) &&
                    post.getBlog().getBlogId().equals(blogId)) {
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

            Long nextPostIdx = null;
            if (!writePost.getIsTemp()) { // 발행되는 포스트 -> index 저장
                nextPostIdx = blog.getPostIdxCnt() + 1;

                // 블로그 postIdxCnt 1 증가
                blog.updatePostIdxCnt(nextPostIdx);
            }

            Post createdPost = postRepository.save(Post.builder()
                    .title(writePost.getTitle())
                    .content(writePost.getContent())
                    .description(writePost.getDescription())
                    .blog(blog)
                    .isTemp(writePost.getIsTemp())
                    .isPublic(writePost.getIsPublic())
                    .index(nextPostIdx)
                    .date(date)
                    .juneberryFile(resFile)
                    .build());

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

            Post post = postRepository.findFirstByPostUid(id).orElseGet(() -> storePost(writePost));

            if (!post.getBlog().getBlogId().equals(writePost.getBlogId())) {
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
                Long nextPostIdx = post.getBlog().getPostIdxCnt() + 1;

                post.updateIndex(nextPostIdx);
                post.getBlog().updatePostIdxCnt(nextPostIdx);
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
    public Optional<Post> getPostById(String blogId, UUID id) {
        Optional<Post> optionalPost = postRepository.findFirstByPostUid(id);

        if (optionalPost.isPresent() && !optionalPost.get().getBlog().getBlogId().equals(blogId)) {
            return Optional.empty();
        }

        return optionalPost;
    }

    @Override
    @Transactional
    public Optional<Post> getPostByIndex(PostRequestDto.SearchPostByIndex searchPostByIndex) {
        Optional<Post> optionalPost = postRepository.findPostByIndex(searchPostByIndex);
        return optionalPost;
    }

    @Override
    @Transactional
    public List<Post> getPostList(PostRequestDto.SearchPostList searchPostList, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("modDt").descending());
        return postRepository.searchPostList(searchPostList, pageable);
    }

    @Override
    @Transactional
    public long getTempPostCnt(String blogId) {
        return postRepository.countByTempPost(blogId);
    }
}
