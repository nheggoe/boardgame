val javafxVersion = "25"

plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = javafxVersion
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("dev.nheggoe.boardgame.app.Launcher")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":monopoly"))
    implementation(project(":snake"))
}

tasks.named<JavaExec>("run") {
    notCompatibleWithConfigurationCache(
        "avaExec run wires JavaFX runtime and touches disallowed types under CC."
    )
    jvmArgs("--enable-native-access=javafx.graphics")
}