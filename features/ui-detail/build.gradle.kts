plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.domain)
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(Dep.AndroidX.Lifecycle.viewModelKtx)
    implementation(Dep.AndroidX.Hilt.compose)

    // Compose
    implementation(Dep.AndroidX.Compose.animation)
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)

    implementation(Dep.Coil.compose)
    implementation(Dep.Accompanist.insets)
    implementation(Dep.Accompanist.systemUi)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.timber)
}

kapt {
    correctErrorTypes = true
}
