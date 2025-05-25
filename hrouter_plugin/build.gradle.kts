plugins{
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

kotlin {
    jvmToolchain(17)
}
repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("hrouterPlugin") {
            id = "com.lq.hrouter"
            implementationClass = "com.lq.hrouter_plugin.HRouterPlugin"
        }
    }
}
