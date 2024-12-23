plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.ramil.customqrscanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.ramil.customqrscanner"
        minSdk = 27
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.android.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)

    //constraint layout
    implementation(libs.androidx.constraint.layout)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //dagger
    implementation(libs.com.google.dagger)
    implementation(libs.com.google.dagger.support)
    ksp(libs.com.google.dagger.processor)
    ksp(libs.com.google.dagger.compiler)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.google.accompanist.permissions)

    implementation(libs.zxing.android.embedded)

    implementation("androidx.camera:camera-core:1.5.0-alpha03")
    implementation("androidx.camera:camera-camera2:1.5.0-alpha03")
    implementation("androidx.camera:camera-lifecycle:1.5.0-alpha03")
    implementation("androidx.camera:camera-view:1.5.0-alpha03")
    implementation("androidx.camera:camera-extensions:1.5.0-alpha03")

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    /*    testImplementation(libs.mockito.inline)
        testImplementation(libs.assertj.core)
        testImplementation(libs.testng)
        testImplementation(libs.androidx.core)*/

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}