package sk.mkiss.interview.pss.controller.v1

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import sk.mkiss.interview.pss.service.SearchService
import sk.mkiss.interview.pss.testutils.mockProduct
import java.math.BigDecimal
import java.util.*

internal class ProductControllerTest {

    private var searchService = mockk<SearchService>()

    private var productController = ProductController(searchService)

    @Test
    fun `Should search products without filter`() {

        every { searchService.search(any(), any()) } returns emptyList()

        productController.getProducts(null, null)

        verify { searchService.search(null, null) }
    }

    @Test
    fun `Should search products with filters`() {

        every { searchService.search(any(), any()) } returns emptyList()

        productController.getProducts(listOf("Brand"), true)

        verify { searchService.search(listOf("Brand"), true) }
    }

    @Test
    fun `Should return products grouped by brand`() {

        every { searchService.search(null, null) } returns listOf(
                mockProduct("name 1", BigDecimal.TEN, "BRAND X", false, 1),
                mockProduct("name 2", BigDecimal.ZERO, "BRAND X", false, 2),
                mockProduct("name 3", BigDecimal.TEN, "BRAND A", false, 3),
                mockProduct("name 4", BigDecimal.ONE, "BRAND X", false, 4)
        )

        val products: SortedMap<String, List<ProductDto>> = productController.getProducts(null, null)

        assertThat(products).isEqualTo(
                sortedMapOf(
                        "BRAND A" to listOf(ProductDto(3, "name 3", BigDecimal.TEN)),
                        "BRAND X" to listOf(
                                ProductDto(2, "name 2", BigDecimal.ZERO),
                                ProductDto(4, "name 4", BigDecimal.ONE),
                                ProductDto(1, "name 1", BigDecimal.TEN)
                        )
                )
        )
    }

    @Test
    fun `Brands should be sorted alphabetically`() {

        every { searchService.search(null, null) } returns listOf(
                mockProduct("name 1", BigDecimal.ONE, "BRAND C", false, 1),
                mockProduct("name 2", BigDecimal.ONE, "BRAND B", false, 2),
                mockProduct("name 3", BigDecimal.ONE, "BRAND A", false, 3),
                mockProduct("name 4", BigDecimal.ONE, "BRAND A", false, 4)
        )

        val products: SortedMap<String, List<ProductDto>> = productController.getProducts(null, null)

        assertThat(products.keys).hasSize(3)
        assertThat(products.keys).containsSequence("BRAND A", "BRAND B", "BRAND C")
    }

    @Test
    fun `Products inside a brand should be sorted ascending by price`() {

        every { searchService.search(null, null) } returns listOf(
                mockProduct("name 1", BigDecimal.TEN, "BRAND X", false, 1),
                mockProduct("name 2", BigDecimal.ZERO, "BRAND X", false, 2),
                mockProduct("name 3", BigDecimal.ONE, "BRAND X", false, 3)
        )

        val products: SortedMap<String, List<ProductDto>> = productController.getProducts(null, null)

        assertThat(products["BRAND X"]).isEqualTo(
                listOf(
                        ProductDto(2, "name 2", BigDecimal.ZERO),
                        ProductDto(3, "name 3", BigDecimal.ONE),
                        ProductDto(1, "name 1", BigDecimal.TEN)
                )
        )
    }

    @Test
    fun `Products should be properly mapped to DTOs`() {

        every { searchService.search(null, null) } returns listOf(
                mockProduct("name 1", BigDecimal.TEN, "BRAND X", true, 1),
                mockProduct("name 2", BigDecimal.ZERO, "BRAND X", false, 2)
        )

        val products: SortedMap<String, List<ProductDto>> = productController.getProducts(null, null)

        assertThat(products["BRAND X"]).isEqualTo(
                listOf(
                        ProductDto(2, "name 2", BigDecimal.ZERO),
                        ProductDto(1, "name 1", BigDecimal.TEN, "ON SALE")
                )
        )
    }
}