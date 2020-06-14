package sk.mkiss.interview.pss.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import sk.mkiss.interview.pss.controller.v1.ProductDto
import sk.mkiss.interview.pss.testutils.mockProduct
import java.math.BigDecimal

internal class ProductMapperTest {

    @Test
    fun `Should set event to "ON SALE" if the product is on sale`() {

        val productEntity = mockProduct("", BigDecimal.ZERO, "", onSale = true, mockedId = 1)

        val dto = ProductMapper.toDto(productEntity)

        assertThat(dto.event).isEqualTo("ON SALE")
    }

    @Test
    fun `Should set event to null if the product is not on sale`() {

        val productEntity = mockProduct("", BigDecimal.ZERO, "", onSale = false, mockedId = 1)

        val dto = ProductMapper.toDto(productEntity)

        assertThat(dto.event).isNull()
    }

    @Test
    fun `Should map product to DTO`() {

        val productEntity = mockProduct("name X", BigDecimal.valueOf(12.49), "brand X", onSale = false, mockedId = 1)

        val dto = ProductMapper.toDto(productEntity)

        assertThat(dto).isEqualTo(ProductDto(1, "name X", BigDecimal("12.49")))
    }
}