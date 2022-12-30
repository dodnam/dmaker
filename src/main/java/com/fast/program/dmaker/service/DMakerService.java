package com.fast.program.dmaker.service;

import com.fast.program.dmaker.dto.CreateDeveloper;
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

import static com.fast.program.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.fast.program.dmaker.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEAR_NOT_MATCHED;

@Service
@RequiredArgsConstructor // service와 controller에 쓰면 좋다. 자동으로 repository를 여기에 injection함.
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final EntityManager em;

    @Transactional
    public void createDeveloper(CreateDeveloper.Request request) {

        validateCreateDeveloperRequest(request);
        // business logic start
        Developer developer = Developer.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .experienceYears(2)
                .name("song")
                .age(5)
                .build();

        developerRepository.save(developer);
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
                    .ifPresent((developer -> { // findByMemberId로 developer를 확인해보고, 이미 있으면 exception발생
                        throw new DMakerException(DUPLICATED_MEMBER_ID);
                    }));
    }
}
