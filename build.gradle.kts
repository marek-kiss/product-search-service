import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.0.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.72"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

group = "sk.mkiss.interview"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.postgresql:postgresql")
	implementation("org.flywaydb:flyway-core:6.4.3")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.testcontainers:postgresql:1.14.3")
	testImplementation("io.mockk:mockk:1.10.0")
	testImplementation("io.rest-assured:rest-assured:3.1.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
