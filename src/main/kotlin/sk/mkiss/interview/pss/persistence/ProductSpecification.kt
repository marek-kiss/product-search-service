package sk.mkiss.interview.pss.persistence

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ProductSpecification(
        val onSale: Boolean? = null,
        val brands: List<String>? = null
) : Specification<ProductEntity> {

    private fun onSale(onSale: Boolean?): Specification<ProductEntity>? {
        return onSale?.let {
            Specification { root, _, cb -> cb.equal(root.get<Boolean>("onSale"), onSale) }
        }
    }

    private fun withBrandIn(brands: Collection<String>?): Specification<ProductEntity>? {
        return brands
                ?.takeIf { it.isNotEmpty() }
                ?.let { Specification { root, _, _ -> root.get<String>("brand").`in`(brands) } }
    }

    override fun toPredicate(root: Root<ProductEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        return Specification.where(onSale(onSale))!!.and(withBrandIn(brands))!!.toPredicate(root, query, criteriaBuilder)
    }
}