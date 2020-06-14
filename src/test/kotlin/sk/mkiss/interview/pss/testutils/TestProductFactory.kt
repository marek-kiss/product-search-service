package sk.mkiss.interview.pss.testutils

import io.mockk.every
import io.mockk.spyk
import sk.mkiss.interview.pss.persistence.ProductEntity
import java.math.BigDecimal

fun mockProduct(
        name: String,
        price: BigDecimal,
        brand: String,
        onSale: Boolean,
        mockedId: Long
): ProductEntity {
    return spyk(ProductEntity(name, price, brand, onSale)).apply { every { id } returns mockedId }
}