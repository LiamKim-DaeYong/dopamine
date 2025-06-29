package io.dopamine.response.common.factory

import io.dopamine.core.code.CommonSuccessCode
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.metadata.PagingMetaContributor
import io.dopamine.response.common.model.PagingMeta
import io.dopamine.response.common.support.DummyMessageResolver
import io.dopamine.response.common.support.DummyMetadataResolver
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.shouldBe
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class DopamineResponseFactoryPagingTest :
    FunSpec({

        val page = PageImpl(listOf("a", "b"), PageRequest.of(1, 2), 10)

        test("should include paging meta when data is Page and includePaging is true") {
            val factory =
                DopamineResponseFactory(
                    props =
                        ResponseProperties(
                            includeMeta = true,
                            metaOptions =
                                ResponseProperties.MetaOptions(
                                    includePaging = true,
                                ),
                        ),
                    resolver = DummyMetadataResolver(),
                    messageResolver = DummyMessageResolver(),
                    contributors = listOf(PagingMetaContributor(includePaging = true)),
                )

            val response = factory.success(data = page, responseCode = CommonSuccessCode.SUCCESS)

            response.meta?.shouldContainKey("paging")
            response.meta?.get("paging") shouldBe
                PagingMeta(
                    page = 1,
                    size = 2,
                    hasNext = true,
                    hasPrevious = true,
                    isFirst = false,
                    isLast = false,
                    totalPages = 5,
                    totalElements = 10,
                )
        }

        test("should not include paging meta when includePaging is false") {
            val factory =
                DopamineResponseFactory(
                    props =
                        ResponseProperties(
                            includeMeta = true,
                            metaOptions =
                                ResponseProperties.MetaOptions(
                                    includePaging = false,
                                ),
                        ),
                    resolver = DummyMetadataResolver(),
                    messageResolver = DummyMessageResolver(),
                    contributors = listOf(PagingMetaContributor(includePaging = false)),
                )

            val response = factory.success(data = page, responseCode = CommonSuccessCode.SUCCESS)

            response.meta?.containsKey("paging") shouldBe false
        }
    })
