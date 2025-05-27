plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android") // Hilt plugin
    id("kotlin-kapt") // Required for annotation processing
    id("com.google.gms.google-services") // Firebase Services
}

android {
    namespace = "com.example.cartzy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cartzy"
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
}

dependencies {

// Networking
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")

// JSON parsing
    implementation ("org.json:json:20231013") // or latest

// Retrofit and Gson converter
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

// Coil for image loading in Compose
    implementation("io.coil-kt:coil-compose:2.4.0")


    implementation("com.google.accompanist:accompanist-pager:0.33.2-alpha")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.33.2-alpha")

    implementation("com.airbnb.android:lottie-compose:6.0.0")


// Jetpack Compose animation + navigation
    implementation ("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.compose.animation:animation:1.5.4")

// Lottie (optional, only if you want to use Lottie animations)
    implementation ("com.airbnb.android:lottie-compose:6.0.0")


// Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

// Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

// Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

// Firebase Services
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")


// Google APIs
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

// Payment Gateway (Razorpay)
    implementation("com.razorpay:checkout:1.6.26")

// Dependency Injection (Hilt)
    implementation("com.google.dagger:hilt-android:2.50")
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.animation.core.lint)
    kapt("com.google.dagger:hilt-compiler:2.50")

// Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}