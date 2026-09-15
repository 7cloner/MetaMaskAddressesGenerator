plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.cloner.metamaskaddressgenerator"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.cloner.metamaskaddressgenerator"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0.0001"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/FastDoubleParser-LICENSE"
            excludes += "/META-INF/FastDoubleParser-NOTICE"
            excludes += "/META-INF/thirdparty-LICENSE"
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/DISCLAIMER"
            excludes += "/META-INF/io.netty.versions.properties"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":metamaskaddressesgenerator"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}