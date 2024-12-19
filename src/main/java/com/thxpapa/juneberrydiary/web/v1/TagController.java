package com.thxpapa.juneberrydiary.web.v1;

import com.thxpapa.juneberrydiary.domain.post.Tag;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.tag.TagResponseDto;
import com.thxpapa.juneberrydiary.service.post.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/blog/{blogId}/tag")
public class TagController {
    private final TagService tagService;
    private final ResponseDto responseDto;

    @Operation(summary = "태그 목록 조회", description = "블로그에 해당하는 모든 태그 목록을 조회합니다.")
    @GetMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTags(
            @Parameter(required = true, description = "블로그 아이디")
            @PathVariable("blogId") String blogId) {
        try {
            List<Tag> tagList = tagService.getAllTags(blogId);

            List<TagResponseDto.TagInfo> tagInfoList = tagList.stream()
                    .map(tag -> TagResponseDto.TagInfo.builder()
                            .name(tag.getName())
                            .build())
                    .collect(Collectors.toList());

            return responseDto.success(tagInfoList);
        } catch (Exception e) {
            log.debug("getAllTags error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
