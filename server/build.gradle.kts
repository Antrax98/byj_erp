//import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
    //id("com.gradleup.shadow") version "8.3.6"
    //id("com.github.johnrengelman.shadow") version "8.1.1"
    //id("com.google.cloud.tools.appengine") version "2.8.3"
}

group = "dev.byjtech.erp"
version = "1.0.0"
application {
    mainClass.set("dev.byjtech.erp.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    //descomentar si se necesitan los tests para el server
    //testImplementation(libs.ktor.server.tests)
    //testImplementation(libs.kotlin.test.junit)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.mysql.driver)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.javatime)
    implementation(libs.hikaricp)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.dotenv)
    implementation(libs.caffeine)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)

    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.identity.jvm)
    implementation(libs.auth0.jwt)
    
    // Email dependencies
    implementation("org.simplejavamail:simple-java-mail:8.3.1")
    implementation("org.simplejavamail:batch-module:8.3.1")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

tasks {
    create("stage").dependsOn("shadowJar")

    build {
        dependsOn(shadowJar)
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveBaseName.set("server")
    archiveClassifier.set("all")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "dev.byjtech.erp.ApplicationKt"
    }
}

//
//configure<AppEngineAppYamlExtension> {
//    stage {
//        setArtifact("server/build/libs/server.jar")
//        //setArtifact("build/libs/${project.name}-all.jar")
//    }
//    deploy {
//        version = "GCLOUD_CONFIG"
//        projectId = "GCLOUD_CONFIG"
//    }
//}