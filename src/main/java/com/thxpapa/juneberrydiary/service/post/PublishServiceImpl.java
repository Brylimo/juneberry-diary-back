package com.thxpapa.juneberrydiary.service.post;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.post.PostFile;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
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

    @Override
    @Transactional
    public JuneberryFile uploadImage(JuneberryUser user, String postId, MultipartFile multipartFile) {
        try {
            JuneberryFile res = null;

            UUID id = UUID.fromString(postId);
            Post post = postRepository.findPostByJuneberryUserAndPostUid(user, id).orElseGet(null);

            if (multipartFile != null && post != null && !multipartFile.isEmpty() && !Objects.isNull(multipartFile.getOriginalFilename())) {
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
    public Post storePost(JuneberryUser user, PostRequestDto.WritePost writePost) {
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

            Post createdPost = postRepository.save(Post.builder()
                    .title(writePost.getTitle())
                    .content(writePost.getContent())
                    .isTemp(writePost.getIsTemp())
                    .date(date)
                    .juneberryUser(user)
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
    public Post updatePost(JuneberryUser user, PostRequestDto.WritePost writePost) {
        try {
            UUID id = UUID.fromString(writePost.getPostId());
            MultipartFile thumbnail = writePost.getThumbnailImg();

            Post post = postRepository.findPostByJuneberryUserAndPostUid(user, id).orElseGet(() -> storePost(user, writePost));
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
    public Optional<Post> getPostById(JuneberryUser user, UUID id) {
        Optional<Post> optionalPost = postRepository.findPostByJuneberryUserAndPostUid(user, id);
        return optionalPost;
    }

    @Override
    @Transactional
    public List<Post> getTempPostList(JuneberryUser user, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("modDt").descending());
        return postRepository.findPostByJuneberryUserAndIsTemp(user, true, pageable);
    }

    @Override
    @Transactional
    public long getTempPostCnt(JuneberryUser user) {
        return postRepository.countByIsTemp(true);
    }
}
