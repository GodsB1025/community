package com.smhrdc.community

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class CommunityApplication

fun main(args: Array<String>) {
	// 1. .env 파일 로드
	val dotenv = dotenv {
		ignoreIfMissing = true // .env 파일이 없어도 에러를 발생시키지 않음 (선택)
	}

	// 2. .env의 모든 변수를 System Property로 설정
	dotenv.entries().forEach { entry ->
		System.setProperty(entry.key, entry.value)
	}

	// 3. Spring 애플리케이션 실행
	runApplication<CommunityApplication>(*args)
}