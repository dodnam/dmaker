package com.fast.program.dmaker.controller;

import com.fast.program.dmaker.dto.CreateDeveloper;
import com.fast.program.dmaker.dto.DeveloperDetailDto;
import com.fast.program.dmaker.dto.DeveloperDto;
import com.fast.program.dmaker.dto.EditDeveloper;
import com.fast.program.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public List<DeveloperDto> getAllDevelopers() {
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllDevelopers();
    }

    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getDeveloper(@PathVariable String memberId) {

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(
            @Valid @RequestBody CreateDeveloper.Request request
            ) {

        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/developers/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request) {

        return dMakerService.editDeveloper(memberId, request);
    }
}
