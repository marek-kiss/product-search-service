package sk.mkiss.interview.pss.persistence

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository

interface ProductRepository : CrudRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity>

