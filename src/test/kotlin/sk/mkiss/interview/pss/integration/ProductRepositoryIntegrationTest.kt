package sk.mkiss.interview.pss.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import sk.mkiss.interview.pss.persistence.ProductEntity
import sk.mkiss.interview.pss.persistence.ProductRepository
import sk.mkiss.interview.pss.persistence.ProductSpecification
import sk.mkiss.interview.pss.testutils.PostgresContainerInitializer
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("integration-test")
@ContextConfiguration(initializers = [PostgresContainerInitializer::class])
class ProductRepositoryIntegrationTest {

    private val BRAND_1 = "Brand 1"
    private val BRAND_2 = "Brand 2"
    private val BRAND_3 = "Brand 3"

    @Autowired
    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setup() {
        productRepository.saveAll(
                listOf(
                        initProduct(brand = BRAND_1, onSale = true),
                        initProduct(brand = BRAND_1, onSale = false),
                        initProduct(brand = BRAND_2, onSale = false),
                        initProduct(brand = BRAND_3, onSale = true)
                )
        )
    }

    private fun initProduct(brand: String, onSale: Boolean): ProductEntity {
        return ProductEntity(
                name = "name",
                price = BigDecimal.TEN,
                brand = brand,
                onSale = onSale
        )
    }

    @AfterEach
    fun cleanup() {
        productRepository.deleteAll()
    }

    @Test
    fun `Repository should find all products`() {

        val results = productRepository.findAll(ProductSpecification())

        assertThat(results).hasSize(4)
    }

    @Test
    fun `Repository should find products by onSale`() {

        val results = productRepository.findAll(ProductSpecification(onSale = false))

        assertThat(results).hasSize(2)

        assertThat(results).allSatisfy { assertThat(it.onSale).isFalse() }
    }

    @Test
    fun `Repository should find products by brand`() {

        val results = productRepository.findAll(ProductSpecification(brands = listOf(BRAND_1)))

        assertThat(results).hasSize(2)

        assertThat(results).allSatisfy { assertThat(it.brand).isEqualTo(BRAND_1) }
    }

    @Test
    fun `Repository should not filter by brand if the list of brands is empty`() {

        val results = productRepository.findAll(ProductSpecification(brands = emptyList()))

        assertThat(results).hasSize(productRepository.count().toInt())
    }

    @Test
    fun `Repository should find products by multiple brands`() {

        val results = productRepository.findAll(ProductSpecification(brands = listOf(BRAND_2, BRAND_3)))

        assertThat(results).hasSize(2)

        assertThat(results).anySatisfy { assertThat(it.brand).isEqualTo(BRAND_2) }

        assertThat(results).anySatisfy { assertThat(it.brand).isEqualTo(BRAND_3) }
    }

    @Test
    fun `Repository should find products by brand and onSale`() {

        val results = productRepository.findAll(ProductSpecification(brands = listOf(BRAND_1), onSale = true))

        assertThat(results).hasSize(1)

        assertThat(results).allSatisfy {
            assertThat(it.brand).isEqualTo(BRAND_1)
            assertThat(it.onSale).isTrue()
        }
    }
}