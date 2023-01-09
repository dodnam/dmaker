package com.fast.program.dmaker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // 특정 row가 생기거나 없어질 때 이 값들을 알아서 변경해줌(생성 시점, 수정 시점을 자동 저장)
public class JpaConfig {

}
