package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.dto.post.PostResponseDto;
import com.thxpapa.juneberrydiary.service.post.PublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
public class PostController {
    private final PublishService publishService;
    private final ResponseDto responseDto;
    @PostMapping(value = "/uploadPostImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPostImage(
            @RequestParam("postId") String postId,
            @RequestPart("editorImg") MultipartFile file,
            @AuthenticationPrincipal JuneberryUser juneberryUser)
    {
        try {
            JuneberryFile juneberryFile = publishService.uploadImage(juneberryUser, postId, file);

            return responseDto.success(PostResponseDto.imageInfo.builder()
                            .imagePath(juneberryFile.getPath())
                            .build());
        } catch (Exception e) {
            log.debug("uploadPostImage error occurred!", e);
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getTempPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTempPost(@RequestParam("id") String id, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        try {
            Optional<Post> post = publishService.getTempPostById(juneberryUser, UUID.fromString(id));

            if (post.isEmpty()) {
                return responseDto.success("해당 post를 찾을 수 없습니다.");
            } else {
                return responseDto.success(PostResponseDto.postInfo.builder()
                        .id(post.get().getPostUid().toString())
                        .title(post.get().getTitle())
                        .content(post.get().getContent())
                        .isTemp(post.get().getIsTemp())
                        .updatedDateTime(post.get().getModDt())
                        .build());
            }
        } catch (Exception e) {
            log.debug("getTempPost erorr occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPost(@RequestBody PostRequestDto.WritePost writePost, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        try {
            Post post = publishService.storePost(juneberryUser, writePost);
            return responseDto.success(PostResponseDto.postInfo.builder()
                            .id(post.getPostUid().toString())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .isTemp(post.getIsTemp())
                            .updatedDateTime(post.getModDt())
                            .build());
        } catch (Exception e) {
            log.debug("addPost error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updatePost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@RequestBody PostRequestDto.WritePost writePost, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        try {
            Post post = publishService.updatePost(juneberryUser, writePost);
            return responseDto.success(PostResponseDto.postInfo.builder()
                    .id(post.getPostUid().toString())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .isTemp(post.getIsTemp())
                    .updatedDateTime(post.getModDt())
                    .build());
        } catch (Exception e) {
            log.debug("updatePost error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
