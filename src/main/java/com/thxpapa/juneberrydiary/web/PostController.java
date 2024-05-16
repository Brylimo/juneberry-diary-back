package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.post.PostRequestDto;
import com.thxpapa.juneberrydiary.service.post.PublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
public class PostController {
    private final PublishService publishService;
    private final ResponseDto responseDto;
    @PostMapping(value = "/uploadImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@RequestBody PostRequestDto.EditorImgData postEditorImageRequestDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        try {
            publishService.uploadImage(juneberryUser, postEditorImageRequestDto);

            return null;
        } catch (Exception e) {
            log.debug("uploadImage error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
