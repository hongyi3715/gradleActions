plugins{
    `kotlin-dsl`
    `java-gradle-plugin`
}

kotlin {
    jvmToolchain(17)
}
repositories {
    mavenCentral()
    google()
}

dependencies {
//    compileOnly("com.android.tools.build:gradle:8.1.4") // 版本与项目一致
}