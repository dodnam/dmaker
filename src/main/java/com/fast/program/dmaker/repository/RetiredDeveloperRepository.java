package com.fast.program.dmaker.repository;

import com.fast.program.dmaker.entity.Developer;
import com.fast.program.dmaker.entity.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetiredDeveloperRepository extends JpaRepository<RetiredDeveloper, Long> {
}
