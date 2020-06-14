package sk.mkiss.interview.pss.service

import io.mockk.every
import sk.mkiss.interview.pss.persistence.ProductRepository
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import sk.mkiss.interview.pss.persistence.ProductSpecification
import sk.mkiss.interview.pss.testutils.mockProduct
import java.math.BigDecimal

internal class SearchServiceTest {

    private val PRODUCT_ENTITY = mockProduct("name", BigDecimal.ZERO, "X", false, 1)

    private var productRepository = mockk<ProductRepository>()

    private var productService = SearchService(productRepository)

    @Test
    fun `should search products in repository without filter`() {
        val specification = slot<ProductSpecification>()
        every { productRepository.findAll(capture(specification)) } returns listOf(PRODUCT_ENTITY)

        val entities = productService.search(null, null)

        assertThat(entities).containsExactly(PRODUCT_ENTITY)

        specification.captured.apply {
            assertThat(brands).isNull()
            assertThat(onSale).isNull()
        }

        verify { productRepository.findAll(any()) }
    }

    @Test
    fun `should search products in repository filtered by brand and onSale`() {
        val specification = slot<ProductSpecification>()
        every { productRepository.findAll(capture(specification)) } returns listOf(PRODUCT_ENTITY)

        val entities = productService.search(brands = listOf("brand"), onSale = true)

        assertThat(entities).containsExactly(PRODUCT_ENTITY)

        specification.captured.apply {
            assertThat(brands).isEqualTo(listOf("brand"))
            assertThat(onSale).isTrue()
        }

        verify { productRepository.findAll(any()) }
    }
}