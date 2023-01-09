package com.fast.program.dmaker.controller;

import com.fast.program.dmaker.dto.DeveloperDto;
import com.fast.program.dmaker.entity.Developer;
import com.fast.program.dmaker.service.DMakerService;
import com.fast.program.dmaker.type.DeveloperLevel;
import com.fast.program.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DMakerControllerTest.class) // controller 관련된 bean들만 올려서 테스트를 진행할 때 쓰기 좋음
@ContextConfiguration(classes = DMakerController.class)
class DMakerControllerTest {
    @Autowired
    private MockMvc mockMvc; // 가상으로 메소드, 바인딩 등 호출

    @MockBean
    private DMakerService dMakerService; // 가짜bean으로 등록

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    @Test
    void getAllDevelopers() throws Exception {
        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                        .developerLevel(DeveloperLevel.SENIOR)
                                .developerSkillType(DeveloperSkillType.BACK_END)
                                        .memberId("memberId1").build();
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .memberId("memberId2").build();
        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(seniorDeveloperDto, juniorDeveloperDto));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(
                        jsonPath("$.[0].developerSkillType",
                                is(DeveloperSkillType.BACK_END.name()))
                ).andExpect(
                        jsonPath("$.[0].developerLevel",
                                is(DeveloperLevel.SENIOR.name()))
                ).andExpect(
                        jsonPath("$.[1].developerSkillType",
                                is(DeveloperSkillType.FRONT_END.name()))
                ).andExpect(
                        jsonPath("$.[1].developerLevel",
                                is(DeveloperLevel.JUNIOR.name()))
                );
    }
}