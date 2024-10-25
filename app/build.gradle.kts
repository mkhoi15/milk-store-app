plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.milk_store_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.milk_store_app"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.converter.scalars)
    implementation(libs.glide)
    implementation(libs.jwtdecode)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.squareup.okhttp3:okhttp:4.6.0")
    implementation("commons-codec:commons-codec:1.14")

    implementation(fileTree(mapOf(
        "dir" to "D:\\LabPRM\\ZPDK_ANdroid",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    compileOnly(libs.lombok)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.lombok)
    implementation(platform("com.google.firebase:firebase-bom:33.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
}
apply(plugin = "com.google.gms.google-services")
