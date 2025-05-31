package io.dopamine.starter.mvc.sample.dto

import java.time.LocalDateTime

data class SampleResponseDto(
    val id: Long,
    val name: String,
    val status: SampleStatus,
    val createdAt: LocalDateTime,
    val tags: List<String>,
    val details: SampleDetailDto,
    val optionalField: String? = null,
)
