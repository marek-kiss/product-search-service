package sk.mkiss.interview.pss.mapper

import sk.mkiss.interview.pss.controller.v1.ProductDto
import sk.mkiss.interview.pss.persistence.ProductEntity

object ProductMapper {

    private const val EVENT_ON_SALE = "ON SALE"

    fun toDto(productEntity: ProductEntity): ProductDto {
        return ProductDto(
                id = productEntity.id!!,
                name = productEntity.name,
                price = productEntity.price,
                event = EVENT_ON_SALE.takeIf { productEntity.onSale }
        )
    }
}