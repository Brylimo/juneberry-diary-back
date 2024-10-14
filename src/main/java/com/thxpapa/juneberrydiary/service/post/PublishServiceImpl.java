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
public class PublishServiceImpl implements PublishService {
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

            Post createdPost = postRepository.save(Post.builder()
                    .title(writePost.getTitle())
                    .content(writePost.getContent())
                    .blog(blog)
                    .isTemp(writePost.getIsTemp())
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
    public List<Post> getTempPostList(String blogId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("modDt").descending());
        return postRepository.searchTempPost(blogId, pageable);
    }

    @Override
    @Transactional
    public long getTempPostCnt(String blogId) {
        return postRepository.countByTempPost(blogId);
    }
}
