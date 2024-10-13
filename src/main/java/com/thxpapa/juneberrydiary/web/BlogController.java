package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.blog.Blog;
import com.thxpapa.juneberrydiary.domain.user.JuneberryUser;
import com.thxpapa.juneberrydiary.dto.ResponseDto;
import com.thxpapa.juneberrydiary.dto.blog.BlogRequestDto;
import com.thxpapa.juneberrydiary.dto.blog.BlogResponseDto;
import com.thxpapa.juneberrydiary.service.blog.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/blog")
public class BlogController {
    private final BlogService blogService;
    private final ResponseDto responseDto;

    @GetMapping(value = "/getBlogById", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBlogById(@RequestParam("id") String id) {
        try {
            Optional<Blog> blog = blogService.getBlogById(id);

            if (blog.isEmpty()) {
                return responseDto.success("해당 blog를 찾을 수 없습니다.");
            } else {
                Blog foundBlog = blog.get();

                return responseDto.success(BlogResponseDto.BlogInfo.builder()
                        .blogId(foundBlog.getBlogId())
                        .blogName(foundBlog.getBlogName())
                        .intro(foundBlog.getIntro())
                        .build());
            }
        } catch (Exception e) {
            log.debug("getBlog error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getAllBlogsByUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBlogsByUser(@AuthenticationPrincipal JuneberryUser juneberryUser) {
        try {
            List<Blog> blogList = blogService.getAllBlogsByUser(juneberryUser);

            List<BlogResponseDto.BlogInfo> blogInfoList = blogList.stream()
                    .map(blog -> BlogResponseDto.BlogInfo.builder()
                            .blogId(blog.getBlogId())
                            .blogName(blog.getBlogName())
                            .intro(blog.getIntro())
                            .build())
                    .collect(Collectors.toList());
            return responseDto.success(blogInfoList);
        } catch (Exception e) {
            log.debug("getAllBlogsByUser error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value="/createBlog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBlog(@RequestBody BlogRequestDto.CreateBlog blogCreateBlogRequestDto, @AuthenticationPrincipal JuneberryUser juneberryUser) {
        try {
            Blog blog = blogService.createBlog(juneberryUser, blogCreateBlogRequestDto);

            return responseDto.success(BlogResponseDto.BlogInfo.builder()
                    .blogId(blog.getBlogId())
                    .blogName(blog.getBlogName())
                    .intro(blog.getIntro())
                    .build());
        } catch (Exception e) {
            log.debug("createBlog error occurred!");
            return responseDto.fail("server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}