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
        vectorDrawables {
            useSupportLibrary = true
        }
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        resources.excludes.add("org/bouncycastle/pqc/crypto/picnic/lowmcL3.bin.properties")
        resources.excludes.add("org/bouncycastle/LICENSE")
    }
}

configurations {
    all {
        resolutionStrategy {
            force("org.bouncycastle:bcprov-jdk15to18:1.80") // Force to use this version to avoid conflicts
        }
    }
}

dependencies {
    // Compose dependencies
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation(libs.androidx.foundation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    // Retrofit Dependencies
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // MetaMask SDK for wallet integration
    implementation("io.metamask.androidsdk:metamask-android-sdk:0.6.6") // MetaMask SDK dependency for Android

    // Web3j (for Ethereum interaction)
    implementation("org.web3j:core:5.0.0") // Web3j for Ethereum interaction

    // New required dependencies for Compose, Activity, and Lifecycle
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.compose.runtime:runtime:1.7.8")
    implementation("androidx.compose.foundation:foundation:1.4.0")

    // WebView support
    implementation("androidx.webkit:webkit:1.13.0")
}
