package com.fast.program.dmaker.service;

import com.fast.program.dmaker.code.StatusCode;
import com.fast.program.dmaker.dto.*;
import com.fast.program.dmaker.entity.Developer;
import com.fast.program.dmaker.entity.RetiredDeveloper;
import com.fast.program.dmaker.exception.DMakerException;
import com.fast.program.dmaker.repository.DeveloperRepository;
import com.fast.program.dmaker.repository.RetiredDeveloperRepository;
import com.fast.program.dmaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.fast.program.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor // service와 controller에 쓰면 좋다. 자동으로 repository를 여기에 injection함.
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;
    private final EntityManager em;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {

        validateCreateDeveloperRequest(request);
        // business logic start
        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .statusCode(StatusCode.EMPLOYED)
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
                    .ifPresent((developer -> { // findByMemberId로 developer를 확인해보고 이미 있으면 exception
                        throw new DMakerException(DUPLICATED_MEMBER_ID);
                    }));
    }


    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity) //Developer를 DeveloperDto로 바꿔주는 mapping
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER)); // orElseThrow()는 만약 값이 없을 때 특정 응답을 한다. 보통 Exception을 발생
    }

    @Transactional // 메소드에 들어가기 전 트랜잭션을 실행한 후, 메소드 실행 후를 db에 반영. 모든 db조작에 넣어주면 좋음
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request, memberId);

        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(NO_DEVELOPER)
        );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }

    private void validateEditDeveloperRequest(EditDeveloper.Request request, String memberId) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(NO_DEVELOPER)
        );
    }

    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 18) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEAR_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNGNIOR
                && experienceYears < 4 || experienceYears > 18) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEAR_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEAR_NOT_MATCHED);
        }
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        // 1. EMPLOYED -> RETIRED
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);

        // 2. save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);
    }
}
