package com.thxpapa.juneberrydiary.web.v1;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.dto.post.PostResponseDto;
import com.thxpapa.juneberrydiary.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
@RequestMapping(value = "/v1/post")
public class PostController {
    private final PostService postService;
    private final ResponseDto responseDto;
    @PostMapping(value = "/uploadPostImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPostImage(
            @RequestParam("blogId") String blogId,
            @RequestParam("postId") String postId,
            @RequestPart("editorImg") MultipartFile file)
    {
        try {
            JuneberryFile juneberryFile = postService.uploadImage(blogId, postId, file);

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
            Optional<Post> post = postService.getPostById(blogId, UUID.fromString(id));

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
                        .index(foundPost.getIndex())
                        .description(foundPost.getDescription())
                        .content(foundPost.getContent())
                        .isTemp(foundPost.getIsTemp())
                        .isPublic(foundPost.getIsPublic())
                        .registeredDateTime(foundPost.getRegDt())
                        .updatedDateTime(foundPost.getModDt())
                        .thumbnailPath(thumbnailPath)
                        .build());
            }
        } catch (Exception e) {
            log.debug("getPost erorr occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getPostByIndex", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostByIndex(@ModelAttribute PostRequestDto.SearchPostByIndex searchPostByIndex) {
        try {
            Optional<Post> optionalPost = postService.getPostByIndex(searchPostByIndex);

            if (optionalPost.isEmpty()) {
                return responseDto.success("해당 post를 찾을 수 없습니다.");
            } else {
                Post foundPost = optionalPost.get();
                String thumbnailPath = null;
                if (foundPost.getJuneberryFile() != null) {
                    thumbnailPath = foundPost.getJuneberryFile().getPath();
                }

                return responseDto.success(PostResponseDto.PostInfo.builder()
                        .id(foundPost.getPostUid().toString())
                        .title(foundPost.getTitle())
                        .index(foundPost.getIndex())
                        .description(foundPost.getDescription())
                        .content(foundPost.getContent())
                        .isTemp(foundPost.getIsTemp())
                        .isPublic(foundPost.getIsPublic())
                        .registeredDateTime(foundPost.getRegDt())
                        .updatedDateTime(foundPost.getModDt())
                        .thumbnailPath(thumbnailPath)
                        .build());
            }
        } catch (Exception e) {
            log.debug("getPostByIndex error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "getPostList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostList(
            @ModelAttribute PostRequestDto.SearchPostList searchPostList,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize)
    {
        try {
            Page<Post> page = postService.getPostList(searchPostList, pageNumber, pageSize);

            List<PostResponseDto.PostInfo> postInfoList = page.getContent().stream()
                    .map(post -> PostResponseDto.PostInfo.builder()
                            .id(post.getPostUid().toString())
                            .isTemp(post.getIsTemp())
                            .isPublic(post.getIsPublic())
                            .title(post.getTitle())
                            .index(post.getIndex())
                            .description(post.getDescription())
                            .content(post.getContent())
                            .registeredDateTime(post.getRegDt())
                            .updatedDateTime(post.getModDt())
                            .thumbnailPath(post.getJuneberryFile() != null ? post.getJuneberryFile().getPath() : null)
                            .build())
                    .collect(Collectors.toList());

            return responseDto.success(PostResponseDto.PostListInfo.builder()
                    .totalCount(page.getTotalElements())
                    .postInfoList(postInfoList)
                    .build());
        } catch (Exception e) {
            log.debug("getPostList erorr occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getTempPostCnt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTempPostCnt(@RequestParam("blogId") String blogId) {
        try {
            long tempCnt = postService.getTempPostCnt(blogId);

            return responseDto.success(tempCnt);
        } catch (Exception e) {
            log.debug("getTempPostCnt error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/addPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPost(@ModelAttribute PostRequestDto.WritePost writePost) {
        try {
            Post post = postService.storePost(writePost);
            return responseDto.success(PostResponseDto.PostInfo.builder()
                            .id(post.getPostUid().toString())
                            .title(post.getTitle())
                            .description(post.getDescription())
                            .content(post.getContent())
                            .isTemp(post.getIsTemp())
                            .isPublic(post.getIsPublic())
                            .registeredDateTime(post.getRegDt())
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
            Post post = postService.updatePost(writePost);
            return responseDto.success(PostResponseDto.PostInfo.builder()
                    .id(post.getPostUid().toString())
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .content(post.getContent())
                    .isTemp(post.getIsTemp())
                    .isPublic(post.getIsPublic())
                    .registeredDateTime(post.getRegDt())
                    .updatedDateTime(post.getModDt())
                    .build());
        } catch (Exception e) {
            log.debug("updatePost error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            UUID postId = UUID.fromString(id);

            postService.deletePostById(postId);
            return responseDto.success("Post deleted successfully");
        } catch (Exception e) {
            log.debug("deletePost error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
