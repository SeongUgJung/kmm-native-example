import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {

    val androidNative by sourceSets.creating {
        dependsOn(sourceSets.getByName("commonMain"))
    }

    val android32 = androidNativeArm32("android32") {
        binaries {
            sharedLib(listOf(NativeBuildType.RELEASE))
        }
    }
    val android64 = androidNativeArm64("android64") {
        binaries {
            sharedLib(listOf(NativeBuildType.RELEASE))
        }
    }

    configure(listOf(android32, android64)) {
        compilations["main"].defaultSourceSet.dependsOn(androidNative)
    }
}

val copyLibs by tasks.creating(Copy::class) {
    val android32 = kotlin.targets["android32"] as KotlinNativeTarget
    val android64 = kotlin.targets["android64"] as KotlinNativeTarget
    val output32 = android32.binaries.getSharedLib(NativeBuildType.RELEASE)
    val output64 = android64.binaries.getSharedLib(NativeBuildType.RELEASE)

    dependsOn(output32.linkTask)
    dependsOn(output64.linkTask)

    destinationDir = file("libs")
    into("armeabi-v7a") {
        from(output32.outputFile)
    }
    into("arm64-v8a") {
        from(output64.outputFile)
    }
}

tasks.getByName("preBuild") {
    dependsOn(copyLibs)
}

android {
    namespace = "com.example.app.native_lib"
    compileSdk = 31
    ndkVersion = "25.2.9519653"
    defaultConfig {
        minSdk = 21
        ndk {
            abiFilters.addAll(setOf("armeabi-v7a", "arm64-v8a"))
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDir(file("libs"))
        }
    }
}