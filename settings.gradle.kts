pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }  // Tambahkan Jitpack untuk mengatasi dependensi yang tidak ditemukan
    }
    plugins {
        id("com.android.application") version "8.1.4" apply false
        id("org.jetbrains.kotlin.android") version "1.9.10" apply false
        id("com.google.dagger.hilt.android") version "2.51" apply false
        id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }  // Tambahkan Jitpack untuk mengatasi dependensi yang tidak ditemukan
    }
}

rootProject.name = "COMPOSE_TA09"
include(":app")
