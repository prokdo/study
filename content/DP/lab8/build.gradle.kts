plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass = "ru.prokdo.MainKt"
}