package sk.mkiss.interview.pss.controller.v1

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProductDto(
        val id: Long,
        val name: String,
        val price: BigDecimal,
        val event: String? = null
)