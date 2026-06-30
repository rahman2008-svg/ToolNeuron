plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace  = "com.dark.download_manager"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
    ndkVersion = "28.2.13676358"

    defaultConfig {
        minSdk = 29  // Android 10 — ForegroundService works; POST_NOTIFICATIONS handled at runtime

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                cppFlags("-std=c++17", "-fstack-protector-strong")
                abiFilters += setOf("arm64-v8a", "x86_64")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            // No pinned "version" here on purpose: pinning to an exact CMake
            // version (e.g. "3.31.4") makes the build fail on any CI/dev
            // machine whose Android SDK doesn't have that exact version
            // pre-installed ([CXX1300] CMake 'X.Y.Z' was not found).
            // Omitting it lets AGP pick/download a compatible CMake automatically.
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

dependencies {
    // ForegroundService, NotificationCompat, startForegroundService
    implementation(libs.androidx.core.ktx)

    // Coroutines: CoroutineScope, Semaphore, Flow, StateFlow
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
