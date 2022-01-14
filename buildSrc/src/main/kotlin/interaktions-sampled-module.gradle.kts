// Inspired by Kord
// https://github.com/kordlib/kord/blob/0.8.x/buildSrc/src/main/kotlin/kord-sampled-module.gradle.kts
plugins {
    kotlin("jvm")
}

sourceSets {
    create("samples") {
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output
    }
}

configurations {
    getByName("samplesImplementation") {
        extendsFrom(configurations["implementation"])
    }
}