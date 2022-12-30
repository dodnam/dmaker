package com.fast.program.dmaker.controller;

import com.fast.program.dmaker.dto.CreateDeveloper;
import com.fast.program.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController // controller+responsebody를 자동으로 달아주는 역할(json의 값으로 응답을 줌)
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("song", "elsa", "olaf");
    }

    @PostMapping("/create-developers")
    public List<String> createDevelopers(
            @Valid @RequestBody CreateDeveloper.Request request
            ) {

        log.info("request : {}", request);

        dMakerService.createDeveloper(request);

        return Collections.singletonList("song");
    }
}
