package io.dopamine.response.common.metadata

import io.dopamine.response.common.model.PagingMeta
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import java.util.function.Function

class PagingMetaContributorTest :
    FunSpec({

        val sampleData = listOf("a", "b")
        val page = PageImpl(sampleData, PageRequest.of(1, 2), 10)

        val slice =
            object : Slice<String> {
                override fun getContent(): List<String> = sampleData

                override fun getNumber(): Int = 1

                override fun getSize(): Int = 2

                override fun getNumberOfElements(): Int = 2

                override fun getSort(): Sort = Sort.unsorted()

                override fun hasContent(): Boolean = true

                override fun iterator(): MutableIterator<String> = sampleData.toMutableList().iterator()

                override fun isFirst(): Boolean = false

                override fun isLast(): Boolean = false

                override fun hasNext(): Boolean = true

                override fun hasPrevious(): Boolean = true

                override fun nextPageable(): Pageable = PageRequest.of(2, 2)

                override fun previousPageable(): Pageable = PageRequest.of(0, 2)

                override fun <U : Any?> map(converter: Function<in String, out U?>): Slice<U?> {
                    val converted = getContent().mapNotNull { converter.apply(it) }
                    return PageImpl(converted, getPageable(), converted.size.toLong())
                }

                override fun getPageable(): Pageable = PageRequest.of(getNumber(), getSize())
            }

        test("should return paging meta when data is Page and includePaging is true") {
            val contributor = PagingMetaContributor(true)
            val result = contributor.contribute(page)

            result["paging"] shouldBe
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

        test("should return paging meta when data is Slice and includePaging is true") {
            val contributor = PagingMetaContributor(true)
            val result = contributor.contribute(slice)

            result["paging"] shouldBe
                PagingMeta(
                    page = 1,
                    size = 2,
                    hasNext = true,
                    hasPrevious = true,
                    isFirst = false,
                    isLast = false,
                    totalPages = null,
                    totalElements = null,
                )
        }

        test("should return empty map when includePaging is false") {
            val contributor = PagingMetaContributor(false)
            contributor.contribute(page) shouldBe emptyMap()
        }

        test("should return empty map when data is not Page or Slice") {
            val contributor = PagingMetaContributor(true)
            contributor.contribute("not paging") shouldBe emptyMap()
        }
    })
