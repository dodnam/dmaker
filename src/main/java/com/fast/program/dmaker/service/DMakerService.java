package com.fast.program.dmaker.service;

import com.fast.program.dmaker.dto.CreateDeveloper;
import com.fast.program.dmaker.dto.DeveloperDetailDto;
import com.fast.program.dmaker.dto.DeveloperDto;
import com.fast.program.dmaker.entity.Developer;
import com.fast.program.dmaker.exception.DMakerErrorCode;
import com.fast.program.dmaker.exception.DMakerException;
import com.fast.program.dmaker.repository.DeveloperRepository;
import com.fast.program.dmaker.type.DeveloperLevel;
import com.fast.program.dmaker.type.DeveloperSkillType;
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
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();
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

        developerRepository.findByMemberId(request.getMemberId())
                    .ifPresent((developer -> { // findByMemberId로 developer를 확인해보고 이미 있으면 exception
                        throw new DMakerException(DUPLICATED_MEMBER_ID);
                    }));
    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity) //Developer를 DeveloperDto로 바꿔주는 mapping
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER)); // orElseThrow()는 만약 값이 없을 때 특정 응답을 한다. 보통 Exception을 발생
    }
}
