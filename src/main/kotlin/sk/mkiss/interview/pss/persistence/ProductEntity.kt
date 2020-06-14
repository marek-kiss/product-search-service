package sk.mkiss.interview.pss.persistence

import java.math.BigDecimal
import javax.persistence.*


@Entity
@Table(name = "product")
class ProductEntity(
        val name: String,
        val price: BigDecimal,
        val brand: String,
        val onSale: Boolean
) {

    @Id
    @GeneratedValue(generator = "product_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq", allocationSize = 1)
    var id: Long? = null
        private set

}