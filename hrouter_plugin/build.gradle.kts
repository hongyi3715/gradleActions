plugins{
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    plugins {
        create("hrouterPlugin") {
            id = "com.lq.hrouter"
            implementationClass = "com.lq.plugin_hrouter.HRouterPlugin"
        }
    }
}
