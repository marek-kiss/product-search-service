package sk.mkiss.interview.pss.service

import org.springframework.stereotype.Service
import sk.mkiss.interview.pss.persistence.ProductEntity
import sk.mkiss.interview.pss.persistence.ProductRepository
import sk.mkiss.interview.pss.persistence.ProductSpecification

@Service
class SearchService(val productRepository: ProductRepository) {

    fun search(brands: List<String>?, onSale: Boolean?): Iterable<ProductEntity> {
        return productRepository.findAll(ProductSpecification(onSale, brands))
    }

}