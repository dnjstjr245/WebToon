plugins {
    `kotlin-dsl`
}

repositories {
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("androidx.*")
            includeGroupByRegex("com.google.testing.platform")
        }
    }
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.0-beta02")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("com.squareup:javapoet:1.13.0")
}
