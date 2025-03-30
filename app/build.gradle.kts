plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.compose_ta09"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.compose_ta09"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    // Updated packaging options
    packaging {
        resources.excludes.add("org/bouncycastle/pqc/crypto/picnic/lowmcL3.bin.properties")
        resources.excludes.add("org/bouncycastle/LICENSE")
    }
}

configurations {
    all {
        resolutionStrategy {
            force ("org.bouncycastle:bcprov-jdk15to18:1.80") // Force to use this version to avoid conflicts
        }
    }
}

dependencies {
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation(libs.androidx.foundation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit Dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // Gson
    implementation("com.google.code.gson:gson:2.8.8")

    // ethers.js Android library (Maven Central)
    implementation("io.kriptal.ethers:ethers-core:1.3.0")

    // walletconnect-android library (Maven Central)
    implementation("com.walletconnect:walletconnect-modal:1.5.11")

    // Add Jitpack for additional dependencies
    implementation("com.walletconnect:android-core:1.35.2") // Example WalletConnect dependency
    implementation("com.github.alexzhirkevich:custom-qr-generator:1.6.2") // Another example dependency
}
