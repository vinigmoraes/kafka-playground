plugins {
    kotlin("jvm") version "1.3.61"
    id("com.github.johnrengelman.shadow") version "5.0.0"
    id("application")
    id("com.commercehub.gradle.plugin.avro") version "0.9.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "br.com.com.bankservice.BankApplicationKt"
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        setUrl("http://packages.confluent.io/maven/")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.koin:koin-ktor:2.0.1")
    implementation("io.ktor:ktor-jackson:1.2.6")
    implementation("io.ktor:ktor-server-netty:1.2.6")
    implementation("org.mongodb:mongo-java-driver:3.12.1")
    implementation("org.apache.kafka:kafka-clients:2.4.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.2")
    implementation("io.confluent:kafka-avro-serializer:5.0.0")
    implementation("org.jetbrains.exposed", "exposed-core", "0.18.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.18.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.18.1")
    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("org.flywaydb:flyway-core:6.1.0")
    implementation("org.postgresql:postgresql:42.2.8")

    testCompile("junit:junit:4.13")

    api("ch.qos.logback:logback-classic:1.2.3")
}

tasks {
    shadowJar {
        manifest {
            attributes(mapOf("Main-Class" to "io.ktor.server.netty.EngineMain"))
        }
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
