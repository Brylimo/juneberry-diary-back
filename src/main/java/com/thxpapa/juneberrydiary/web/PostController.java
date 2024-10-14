package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.dto.post.PostResponseDto;
import com.thxpapa.juneberrydiary.service.post.PublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
public class PostController {
    private final PublishService publishService;
    private final ResponseDto responseDto;
    @PostMapping(value = "/uploadPostImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPostImage(
            @RequestParam("blogId") String blogId,
            @RequestParam("postId") String postId,
            @RequestPart("editorImg") MultipartFile file)
    {
        try {
            JuneberryFile juneberryFile = publishService.uploadImage(blogId, postId, file);

            return responseDto.success(PostResponseDto.ImageInfo.builder()
                            .imagePath(juneberryFile.getPath())
                            .build());
        } catch (Exception e) {
            log.debug("uploadPostImage error occurred!", e);
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPost(@RequestParam("blogId") String blogId, @RequestParam("id") String id) {
        try {
            Optional<Post> post = publishService.getPostById(blogId, UUID.fromString(id));

            if (post.isEmpty()) {
                return responseDto.success("해당 post를 찾을 수 없습니다.");
            } else {
                Post foundPost = post.get();
                String thumbnailPath = null;
                if (foundPost.getJuneberryFile() != null) {
                    thumbnailPath = foundPost.getJuneberryFile().getPath();
                }

                return responseDto.success(PostResponseDto.PostInfo.builder()
                        .id(foundPost.getPostUid().toString())
                        .title(foundPost.getTitle())
                        .description(foundPost.getDescription())
                        .content(foundPost.getContent())
                        .isTemp(foundPost.getIsTemp())
                        .isPublic(foundPost.getIsPublic())
                        .updatedDateTime(foundPost.getModDt())
                        .thumbnailPath(thumbnailPath)
                        .build());
            }
        } catch (Exception e) {
            log.debug("getPost erorr occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "getTempPostList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTempPostList(
            @RequestParam("blogId") String blogId,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize)
    {
        try {
            List<Post> postList = publishService.getTempPostList(blogId, pageNumber, pageSize);

            List<PostResponseDto.PostInfo> postInfoList = postList.stream()
                    .map(post -> PostResponseDto.PostInfo.builder()
                            .id(post.getPostUid().toString())
                            .isTemp(post.getIsTemp())
                            .isPublic(post.getIsPublic())
                            .title(post.getTitle())
                            .description(post.getDescription())
                            .content(post.getContent())
                            .updatedDateTime(post.getModDt())
                            .build())
                    .collect(Collectors.toList());
            return responseDto.success(postInfoList);
        } catch (Exception e) {
            log.debug("getTempPostList erorr occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getTempPostCnt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTempPostCnt(@RequestParam("blogId") String blogId) {
        try {
            long tempCnt = publishService.getTempPostCnt(blogId);

            return responseDto.success(tempCnt);
        } catch (Exception e) {
            log.debug("getTempPostCnt error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPost(@ModelAttribute PostRequestDto.WritePost writePost) {
        try {
            Post post = publishService.storePost(writePost);
            return responseDto.success(PostResponseDto.PostInfo.builder()
                            .id(post.getPostUid().toString())
                            .title(post.getTitle())
                            .description(post.getDescription())
                            .content(post.getContent())
                            .isTemp(post.getIsTemp())
                            .isPublic(post.getIsPublic())
                            .updatedDateTime(post.getModDt())
                            .build());
        } catch (Exception e) {
            log.debug("addPost error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updatePost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@ModelAttribute PostRequestDto.WritePost writePost) {
        try {
            Post post = publishService.updatePost(writePost);
            return responseDto.success(PostResponseDto.PostInfo.builder()
                    .id(post.getPostUid().toString())
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .content(post.getContent())
                    .isTemp(post.getIsTemp())
                    .isPublic(post.getIsPublic())
                    .updatedDateTime(post.getModDt())
                    .build());
        } catch (Exception e) {
            log.debug("updatePost error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
