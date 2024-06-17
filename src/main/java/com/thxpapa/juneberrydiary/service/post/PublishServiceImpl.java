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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
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

            Post createdPost = postRepository.save(Post.builder()
                    .title(writePost.getTitle())
                    .content(writePost.getContent())
                    .isTemp(writePost.getIsTemp())
                    .date(date)
                    .juneberryUser(user)
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
        UUID id = UUID.fromString(writePost.getPostId());
        Post post = postRepository.findPostByJuneberryUserAndPostUid(user, id).orElseGet(() -> storePost(user, writePost));
        post.updatePostByWritePost(writePost);

        return post;
    }

    @Override
    @Transactional
    public Optional<Post> getTempPostById(JuneberryUser user, UUID id) {
        Optional<Post> optionalPost = postRepository.findTempPostByJuneberryUserAndId(user, id);
        return optionalPost;
    }

    @Override
    @Transactional
    public List<Post> getTempPostList(JuneberryUser user, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return postRepository.findPostByJuneberryUserAndIsTemp(user, true, pageable);
    }

    @Override
    @Transactional
    public long getTempPostCnt(JuneberryUser user) {
        return postRepository.countByIsTemp(true);
    }
}
