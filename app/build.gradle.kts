plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Necesario para Room
}

android {
    namespace = "com.example.actividad1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.actividad1"
        minSdk = 28
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
        viewBinding = true
    }

}

dependencies {
    // --- DEPENDENCIAS ADICIONALES PARA EL PROYECTO CRUD ---

    // Dependencia para RecyclerView
    // Asegúrate de usar la última versión estable si necesitas
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // Dependencia para CardView
    // Asegúrate de usar la última versión estable si necesitas
    implementation("androidx.cardview:cardview:1.0.0")
    // Dependencia para Gson (para manejar JSON)
    implementation("com.google.code.gson:gson:2.10.1")

    // --- FIN DEPENDENCIAS ADICIONALES ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- DEPENDENCIAS PARA UBICACIÓN, CÁMARA Y ROOM ---

    // Room components (Base de datos local)
    // Asegúrate de usar la última versión estable (2.6.1 al día de hoy)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1") // Procesador de anotaciones para Room
    implementation("androidx.room:room-ktx:2.6.1") // Extensiones Kotlin para Room (Coroutines)

    // Lifecycle components (ViewModel y LiveData) - Muy recomendadas para arquitectura MVVM con Room
    // Asegúrate de usar la última versión estable (2.8.0 al día de hoy)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    // Kotlin Coroutines (Necesarias para Room-ktx y operaciones asíncronas)
    // Asegúrate de usar la última versión estable (1.7.3 al día de hoy)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Google Location Services (Para ubicación en tiempo real)
    // Asegúrate de usar la última versión estable (21.0.1 al día de hoy)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Glide (Para cargar y mostrar imágenes de la cámara, muy útil)
    // Asegúrate de usar la última versión estable (4.16.0 al día de hoy)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
}