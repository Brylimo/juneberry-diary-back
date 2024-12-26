package com.thxpapa.juneberrydiary.web.v1;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.post.Post;
import com.thxpapa.juneberrydiary.domain.post.PostTag;
import com.thxpapa.juneberrydiary.domain.post.Tag;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.dto.post.PostResponseDto;
import com.thxpapa.juneberrydiary.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping(value = "/v1")
public class PostController {
    private final PostService postService;
    private final ResponseDto responseDto;

    @Operation(summary = "포스트 이미지 저장", description = "포스트 이미지를 저장합니다.")
    @PostMapping(value = "/post/{postId}/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPostImage(
            @Parameter(description = "포스트 아이디")
            @PathVariable("postId") String postId,
            @RequestPart("editorImg") MultipartFile file)
    {
        try {
            JuneberryFile juneberryFile = postService.uploadImage(postId, file);

            return responseDto.success(PostResponseDto.ImageInfo.builder()
                            .imagePath(juneberryFile.getPath())
                            .build());
        } catch (Exception e) {
            log.debug("uploadPostImage error occurred!", e);
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "포스트 조회1", description = "포스트 id에 맞는 포스트를 조회합니다.")
    @GetMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPost(
            @Parameter(description = "포스트 아이디")
            @RequestParam("id") String id) {
        try {
            Optional<PostResponseDto.PostInfo> optionalPostInfo = postService.getPostById(UUID.fromString(id));

            if (optionalPostInfo.isEmpty()) {
                return responseDto.success("해당 post를 찾을 수 없습니다.");
            } else {
                PostResponseDto.PostInfo postInfo = optionalPostInfo.get();
                return responseDto.success(postInfo);
            }
        } catch (Exception e) {
            log.debug("getPost erorr occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "포스트 조회2", description = "인덱스에 맞는 포스트를 조회합니다.")
    @GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostByIndex(@ModelAttribute PostRequestDto.SearchPostByIndex searchPostByIndex) {
        try {
            Optional<PostResponseDto.PostInfo> optionalPostInfo = postService.getPostByIndex(searchPostByIndex);

            if (optionalPostInfo.isEmpty()) {
                return responseDto.success("해당 post를 찾을 수 없습니다.");
            } else {
                PostResponseDto.PostInfo postInfo = optionalPostInfo.get();
                return responseDto.success(postInfo);
            }
        } catch (Exception e) {
            log.debug("getPostByIndex error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "포스트 목록 조회", description = "포스트 목록을 조회합니다.")
    @GetMapping(value = "/post/posts", produces = MediaType.APPLICATION_JSON_VALUE)
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
                            .category(post.getSubCategory().getCategory().getName())
                            .subCategory(post.getSubCategory().getName())
                            .title(post.getTitle())
                            .index(post.getIndex())
                            .description(post.getDescription())
                            .content(post.getContent())
                            .tags(post.getPostTags().stream()
                                    .map(PostTag::getTag)
                                    .map(Tag::getName)
                                    .collect(Collectors.toList()))
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

    @Operation(summary = "임시 포스트 개수 조회", description = "임시 포스트 개수를 조회합니다.")
    @GetMapping(value = "/post/temp/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTempPostCnt(
            @Parameter(description = "블로그 아이디")
            @RequestParam("blogId") String blogId) {
        try {
            PostResponseDto.PostInfo tempPostInfo = PostResponseDto.PostInfo.builder()
                    .isTemp(true)
                    .build();

            long tempCnt = postService.getPostCnt(blogId, tempPostInfo);

            return responseDto.success(tempCnt);
        } catch (Exception e) {
            log.debug("getTempPostCnt error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "포스트 저장", description = "포스트를 저장합니다.")
    @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(summary = "포스트 업데이트", description = "포스트를 업데이트합니다.")
    @PutMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(summary = "포스트 삭제", description = "id에 해당하는 포스트를 삭제합니다.")
    @DeleteMapping(value = "/post/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePost(
            @Parameter(description = "포스트 아이디")
            @PathVariable String id) {
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
