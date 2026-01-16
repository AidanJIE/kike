plugins {
    id("com.android.application")
    kotlin("android") version "1.8.10"  // ¡CONSISTENTE CON LA VERSIÓN QUE YA TIENES!

}

android {
    namespace = "com.example.proyectofinal"
    compileSdk = 34  // Cambia de 35 a 34

    defaultConfig {
        applicationId = "com.example.proyectofinal"
        minSdk = 24
        targetSdk = 34  // Cambia de 35 a 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // ¡¡¡VERSIONES COMPATIBLES CON SDK 34 Y GRADLE 8.8.0!!!

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")  // ¡1.9.0, NO 1.13.0!
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Navigation - versiones viejas compatibles
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")

    // RecyclerView y CardView
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // Activity - ¡VERSIÓN COMPATIBLE! 1.7.2, NO 1.12.1
    implementation("androidx.activity:activity:1.7.2")

    // ¡¡¡SI VES ESTA LÍNEA, ELIMÍNALA COMPLETAMENTE!!!
    // implementation("androidx.navigationevent:navigationevent-android:1.0.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}