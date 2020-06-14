package sk.mkiss.interview.pss.testutils

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class PostgresContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=${PostgresContainerHolder.postgresContainer.jdbcUrl}",
                "spring.datasource.username=${PostgresContainerHolder.postgresContainer.username}",
                "spring.datasource.password=${PostgresContainerHolder.postgresContainer.password}"
        ).applyTo(applicationContext)
    }

}

object PostgresContainerHolder {
    val postgresContainer : PostgresContainer by lazy {
        PostgresContainer().apply { start() }
    }

    class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:11.3")
}


