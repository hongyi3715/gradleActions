plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    id("com.lq.hrouter") version "1.0.0"
}

android {
    namespace = "com.lq.gradletest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lq.gradletest"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(findProperty("signing.store.file") ?: "release.keystore")
            storePassword = findProperty("signing.store.password") as String?
            keyAlias = findProperty("signing.key.alias") as String?
            keyPassword = findProperty("signing.key.password") as String?
        }
    }

    sourceSets["main"].java.srcDirs(
        "build/generated/route",
        "build/generated/interceptor",
        "build/generated/degrade",
        "build/generated/deeplink")

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
    ksp {
        arg("moduleName",project.name)
    }

}

dependencies {
    implementation(project(":lib_annotation"))
    implementation(project(":lib_api"))
    implementation(project(":login"))
    ksp(project(":lib_compiler"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

