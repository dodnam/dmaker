package com.fast.program.dmaker.service;

import com.fast.program.dmaker.code.StatusCode;
import com.fast.program.dmaker.dto.CreateDeveloper;
import com.fast.program.dmaker.dto.DeveloperDetailDto;
import com.fast.program.dmaker.dto.DeveloperDto;
import com.fast.program.dmaker.entity.Developer;
import com.fast.program.dmaker.repository.DeveloperRepository;
import com.fast.program.dmaker.repository.RetiredDeveloperRepository;
import com.fast.program.dmaker.type.DeveloperLevel;
import com.fast.program.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

//@SpringBootTest // 어플리케이션을 띄우는 것과 유사하게 모든 빈을 다 띄워서 같은 환경으로 테스트를 하게 한다. 통합 테스트.
@ExtendWith(MockitoExtension.class) // mokito를 이용한 테스트
class DMakerServiceTest {
    // jnuit은 java를 위한 테스트

//   @Autowired
    @InjectMocks // 가짜Mock을 inject한다.
    private DMakerService dMakerService;

    // 가장의 mock으로 dMakerService 안에서 사용된 repository를 등록해준다.
    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @Test
    public void testSome() {
//        dMakerService.createDeveloper(CreateDeveloper.Request.builder()
//                        .developerLevel(DeveloperLevel.JUNIOR)
//                        .developerSkillType(DeveloperSkillType.BACK_END)
//                        .experienceYears(3)
//                        .memberId("dodnam")
//                        .name("name")
//                        .age(33)
//                        .build());
//        List<DeveloperDto> allEmployedDevelopers = dMakerService.getAllEmployedDevelopers();
//        System.out.println("==================");
//        System.out.println(allEmployedDevelopers);
//        System.out.println("==================");
//
//        // assertEquals("hello world", result); // 가장 많이 사용되는 방법 중 하나. 예상값 , 실제값
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(Developer.builder()
                                .developerSkillType(DeveloperSkillType.BACK_END)
                                .developerLevel(DeveloperLevel.SENIOR)
                                .experienceYears(12)
                                .statusCode(StatusCode.EMPLOYED)
                                .name("name")
                                .age(33)
                                .build()));

        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(DeveloperSkillType.BACK_END, developerDetail.getDeveloperLevel());
        assertEquals(12, developerDetail.getAge());
    }
}