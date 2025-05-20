package io.dopamine.docs.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(DocsPropertyKeys.PREFIX)
data class DocsProperties(
    /**
     * 전체 문서 기능 활성화 여부
     */
    val enabled: Boolean = true,
    /**
     * Swagger 문서 설정
     */
    val swagger: SwaggerProperties = SwaggerProperties(),
)

data class SwaggerProperties(
    /**
     * Swagger 문서 활성화 여부
     */
    val enabled: Boolean = true,
    /**
     * Swagger 그룹 이름
     */
    val group: String = "default",
    /**
     * API 제목
     */
    val title: String = "Dopamine API",
    /**
     * API 설명
     */
    val description: String = "공통 인프라 Swagger 문서",
    /**
     * API 버전
     */
    val version: String = "1.0.0",
    /**
     * Swagger가 적용될 기본 경로
     */
    val basePath: String = "/api/**",
    /**
     * Swagger UI 경로 (/swagger-ui.html → /docs)
     * springdoc.swagger-ui.path 설정에 매핑됨
     */
    val uiPath: String = "/docs"
)
