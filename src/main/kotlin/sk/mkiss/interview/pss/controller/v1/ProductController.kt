package sk.mkiss.interview.pss.controller.v1

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sk.mkiss.interview.pss.mapper.ProductMapper
import sk.mkiss.interview.pss.service.SearchService
import java.util.*

@RestController
@RequestMapping("/api/v1/products")
class ProductController(val searchService: SearchService) {

    @GetMapping
    fun getProducts(
            @RequestParam(name = "brand")
            brands: List<String>?,
            @RequestParam(name = "on_sale")
            onSale: Boolean?
    ): SortedMap<String, List<ProductDto>> {
        return searchService.search(brands, onSale)
                .groupBy({ i -> i.brand }, ProductMapper::toDto)
                .mapValues { it.value.sortedBy(ProductDto::price) }
                .toSortedMap()
    }
}