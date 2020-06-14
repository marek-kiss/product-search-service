package sk.mkiss.interview.pss.integration

import io.restassured.RestAssured
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import sk.mkiss.interview.pss.persistence.ProductEntity
import sk.mkiss.interview.pss.persistence.ProductRepository
import sk.mkiss.interview.pss.testutils.HAMCREST_MATCHER_CONFIG
import sk.mkiss.interview.pss.testutils.JsonStringMatcher
import sk.mkiss.interview.pss.testutils.PostgresContainerInitializer
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@ContextConfiguration(initializers = [PostgresContainerInitializer::class])
class SearchProductsIntegrationTest {

    @Autowired
    private lateinit var productRepository: ProductRepository

    private val baseUrl by lazy { "http://localhost:$randomServerPort" }

    @LocalServerPort
    private var randomServerPort: Int = 0

    @AfterEach
    fun cleanup() {
        productRepository.deleteAll()
    }

    @Test
    fun `Should return products grouped and filtered by brand`() {

        val id1 = productRepository.save(ProductEntity("name 1", BigDecimal("10.05"), "BRAND_X", false)).id
        val id2 = productRepository.save(ProductEntity("name 2", BigDecimal("0.50"), "BRAND_X", false)).id
        val id3 = productRepository.save(ProductEntity("name 3", BigDecimal("10.99"), "BRAND_A", true)).id
        val id4 = productRepository.save(ProductEntity("name 4", BigDecimal("1.10"), "BRAND_X", false)).id
        productRepository.save(ProductEntity("name 5", BigDecimal("1.10"), "BRAND_Z", false))

        RestAssured.given()
                .get("$baseUrl/api/v1/products?brand=BRAND_X&brand=BRAND_A")
                .then()
                .statusCode(200)
                .body(
                        JsonStringMatcher.matchesJson(
                                """{
                                    "BRAND_A": [
                                        {
                                            "id":$id3,
                                            "name":"name 3",
                                            "price":10.99,
                                            "event": "ON SALE"
                                        }
                                    ],
                                    "BRAND_X": [
                                        {
                                            "id":$id2,
                                            "name":"name 2",
                                            "price":0.5
                                        },
                                        {
                                            "id":$id4,
                                            "name":"name 4",
                                            "price":1.1
                                        },
                                        {
                                            "id":$id1,
                                            "name":"name 1",
                                            "price":10.05
                                        }
                                    ]
                                }"""
                        )
                )
    }

    @Test
    fun `Should return all on-sale products grouped by brand`() {

        val id1 = productRepository.save(ProductEntity("name 1", BigDecimal("10.05"), "BRAND_X", false)).id
        val id2 = productRepository.save(ProductEntity("name 2", BigDecimal("0.50"), "BRAND_X", false)).id
        val id3 = productRepository.save(ProductEntity("name 3", BigDecimal("10.99"), "BRAND_A", true)).id
        val id4 = productRepository.save(ProductEntity("name 4", BigDecimal("1.10"), "BRAND_X", false)).id
        val id5 = productRepository.save(ProductEntity("name 5", BigDecimal("1.10"), "BRAND_Z", true)).id

        RestAssured.given()
                .config(HAMCREST_MATCHER_CONFIG)
                .get("$baseUrl/api/v1/products?on_sale=true")
                .then()
                .statusCode(200)
                .body(
                        JsonStringMatcher.matchesJson(
                                """{
                                    "BRAND_A": [
                                        {
                                            "id":$id3,
                                            "name":"name 3",
                                            "price":10.99,
                                            "event": "ON SALE"
                                        }
                                    ],
                                    "BRAND_Z": [
                                        {
                                            "id":$id5,
                                            "name":"name 5",
                                            "price":1.10,
                                            "event": "ON SALE"
                                        }
                                    ]
                                }"""
                        )
                )
    }

}