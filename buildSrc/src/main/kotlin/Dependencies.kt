import org.gradle.api.JavaVersion

object Config {
    const val application_id = "ru.viksimurg.viknote"
    const val compile_sdk = 31
    const val min_sdk = 21
    const val target_sdk = 31
    val java_version = JavaVersion.VERSION_1_8
}

object Releases {
    const val version_code = 1
    const val version_name = "1.0"
}

object Versions{
    //Design
    const val appcompat = "1.3.1"
    const val material = "1.4.0"
    const val constraintlayout = "2.1.2"

    //Navigation
    const val fragment = "2.3.5"
    const val ui = "2.3.5"

    //Kotlin
    const val core = "1.6.0"
    const val stdlib = "1.5.20"
    const val coroutinesCore = "1.4.3"
    const val coroutinesAndroid = "1.4.3"

    //Koin
    const val koinAndroid = "3.1.2"
    const val koinAndroidCompat = "3.1.2"
    const val koinCore = "3.1.2"

    //Room
    const val roomKtx = "2.3.0"
    const val runtime = "2.3.0"
    const val roomCompiler = "2.3.0"

    //Test
    const val jUnit = "4.12"
    const val runner = "1.2.0"
    const val espressoCore = "3.2.0"
    const val ext = "1.1.3"

}

object Design {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
}

object Navigation{
    const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.fragment}"
    const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.ui}"
}

object Kotlin {
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.stdlib}"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"
}

object Koin {
    const val koin_android = "io.insert-koin:koin-android:${Versions.koinAndroid}"
    const val koin_android_compat = "io.insert-koin:koin-android-compat:${Versions.koinAndroidCompat}"
    const val koin_core = "io.insert-koin:koin-core:${Versions.koinCore}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.runtime}"
    const val compiler = "androidx.room:room-compiler:${Versions.roomCompiler}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.roomKtx}"
}

object TestImpl {
    const val junit = "junit:junit:${Versions.jUnit}"
    const val runner = "androidx.test:runner:${Versions.runner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val ext = "androidx.test.ext:junit:${Versions.ext}"
}