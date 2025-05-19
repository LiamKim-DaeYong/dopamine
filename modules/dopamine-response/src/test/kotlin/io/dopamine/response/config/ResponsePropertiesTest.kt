package io.dopamine.response.config

import io.dopamine.response.format.TimestampFormat
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Configuration

class ResponsePropertiesTest :
    FunSpec({

        val baseRunner =
            ApplicationContextRunner()
                .withUserConfiguration(TestConfig::class.java)

        test("기본값이 정확히 바인딩되어야 한다") {
            baseRunner.run {
                val props = it.getBean(ResponseProperties::class.java)
                props.enabled shouldBe true
                props.includeMeta shouldBe true
                props.timestampFormat shouldBe TimestampFormat.ISO_8601
                props.codes.shouldBeEmpty()

                with(props.metaOptions) {
                    includeTraceId shouldBe true
                    traceIdKey shouldBe "traceId"
                    traceIdHeader shouldBe "X-Trace-ID"
                    includePaging shouldBe true
                }
            }
        }

        test("timestampFormat이 COMPACT로 설정될 수 있어야 한다") {
            baseRunner
                .withPropertyValues("dopamine.response.timestamp-format=COMPACT")
                .run {
                    val props = it.getBean(ResponseProperties::class.java)
                    props.timestampFormat shouldBe TimestampFormat.COMPACT
                }
        }

        test("metaOptions가 모두 재정의되어야 한다") {
            baseRunner
                .withPropertyValues(
                    "dopamine.response.meta-options.include-trace-id=false",
                    "dopamine.response.meta-options.trace-id-key=X-Request-ID",
                    "dopamine.response.meta-options.trace-id-header=X-Custom-Trace",
                    "dopamine.response.meta-options.include-paging=false",
                ).run {
                    val meta = it.getBean(ResponseProperties::class.java).metaOptions
                    meta.includeTraceId shouldBe false
                    meta.traceIdKey shouldBe "X-Request-ID"
                    meta.traceIdHeader shouldBe "X-Custom-Trace"
                    meta.includePaging shouldBe false
                }
        }

        test("codes 항목이 여러 개일 때 모두 바인딩되어야 한다") {
            baseRunner
                .withPropertyValues(
                    "dopamine.response.codes[0].http-status=BAD_REQUEST",
                    "dopamine.response.codes[0].code=R400",
                    "dopamine.response.codes[0].message=잘못된 요청입니다.",
                    "dopamine.response.codes[1].http-status=FORBIDDEN",
                    "dopamine.response.codes[1].code=R403",
                    "dopamine.response.codes[1].message=접근 거부됨",
                ).run {
                    val codes = it.getBean(ResponseProperties::class.java).codes
                    codes.size shouldBe 2
                    codes[0].code shouldBe "R400"
                    codes[1].code shouldBe "R403"
                }
        }

        test("enabled=false로 설정될 수 있어야 한다") {
            baseRunner
                .withPropertyValues("dopamine.response.enabled=false")
                .run {
                    val props = it.getBean(ResponseProperties::class.java)
                    props.enabled shouldBe false
                }
        }
    })

@Configuration
@EnableConfigurationProperties(ResponseProperties::class)
class TestConfig
