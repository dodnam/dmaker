package com.fast.program.dmaker.service;

import com.fast.program.dmaker.code.StatusCode;
import com.fast.program.dmaker.dto.CreateDeveloper;
import com.fast.program.dmaker.dto.DeveloperDetailDto;
import com.fast.program.dmaker.dto.DeveloperDto;
import com.fast.program.dmaker.entity.Developer;
import com.fast.program.dmaker.exception.DMakerErrorCode;
import com.fast.program.dmaker.exception.DMakerException;
import com.fast.program.dmaker.repository.DeveloperRepository;
import com.fast.program.dmaker.repository.RetiredDeveloperRepository;
import com.fast.program.dmaker.type.DeveloperLevel;
import com.fast.program.dmaker.type.DeveloperSkillType;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    private final Developer defaultDeveloper = Developer.builder()
            .developerSkillType(DeveloperSkillType.BACK_END)
            .developerLevel(DeveloperLevel.SENIOR)
            .experienceYears(18)
            .memberId("memberId1")
            .name("name")
            .age(33)
            .build();

    private final CreateDeveloper.Request defaultCreateRequest = CreateDeveloper.Request.builder()
            .developerSkillType(DeveloperSkillType.BACK_END)
            .developerLevel(DeveloperLevel.SENIOR)
            .experienceYears(18)
            .memberId("memberId1")
            .name("name")
            .age(33)
            .build();

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

        // given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(Developer.builder()
                                .developerSkillType(DeveloperSkillType.BACK_END)
                                .developerLevel(DeveloperLevel.SENIOR)
                                .experienceYears(18)
                                .statusCode(StatusCode.EMPLOYED)
                                .name("name")
                                .age(33)
                                .build()));
        // when
        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        // then
        assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(DeveloperSkillType.BACK_END, developerDetail.getDeveloperSkillType());
        assertEquals(18, developerDetail.getExperienceYears());
    }

    @Test
    void CreateDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty()); // return이 없어야 중복이 없으므로
        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

        //when
        dMakerService.createDeveloper(defaultCreateRequest);

        //then
        verify(developerRepository, times(1)) // 동작 횟수
                .save(captor.capture()); // developerRepository의 save를 할 때 캡쳐된 데이터를 뽑을 수 있다
        Developer savedDeveloper = captor.getValue();
        assertEquals(DeveloperLevel.SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.BACK_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(18, savedDeveloper.getExperienceYears());

    }

    @Test
    void CreateDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper)); // return이 없어야 중복이 없으므로
        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

        // 동작과 검증을 한 번에 한다.
        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                                    () -> dMakerService.createDeveloper(defaultCreateRequest));

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());

    }
}