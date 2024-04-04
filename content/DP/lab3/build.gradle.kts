plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "ru.prokdo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}