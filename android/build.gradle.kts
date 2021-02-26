import Libs.kotlinVersion

repositories {
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(Libs.Compose.ui)
    implementation(Libs.Compose.uiTooling)
    implementation(Libs.Compose.foundation)
    implementation(Libs.Compose.material)
    implementation(Libs.Compose.iconsCore)
    implementation(Libs.Compose.iconsExt)
    implementation(Libs.Compose.activity)
    implementation(Libs.Compose.viewModels)

    implementation(Libs.Y.core)
    implementation(Libs.Y.collections)

    implementation(Libs.Kotlinx.coroutines)

    implementation(Libs.Kotlinx.datetime)

    implementation(Libs.Ktor.core)
    implementation(Libs.Ktor.engine)
    implementation(Libs.Ktor.gson)

    implementation(Libs.Ktor.mock)

    implementation(Libs.Picasso.picasso)

    implementation(Libs.Paging.compose)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    debugImplementation(Libs.Compose.uiTooling)
    debugImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    testImplementation(Libs.Kotest.runner)
    testImplementation(Libs.Kotest.assertions)
    testImplementation(Libs.Kotest.property)

    testImplementation(Libs.Compose.uiTestJUnit)

    testImplementation(Libs.Kotlinx.coroutinesTest)
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "com.why.githubtrendyrepos"
        multiDexEnabled = true
        minSdkVersion(22)
        targetSdkVersion(30)
        versionCode = 1
        versionName = Ci.publishVersion
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = Libs.jvmTarget
        useIR = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Libs.Compose.version
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
