package com.fast.program.dmaker.repository;

import com.fast.program.dmaker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByMemberId(String memberId);
    // 여기까지만 해도 db에 저장하는 repository에 저장할 수 있는 jpa기능을 활용할 수 있다.
}
