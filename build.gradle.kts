plugins {
    java
    id("org.springframework.boot") version "3.5.0"
}

group = "com.neup.web"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.5.0"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
    implementation("org.springframework.security:spring-security-crypto:6.5.0")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}