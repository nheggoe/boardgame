import org.gradle.api.tasks.testing.logging.TestLogEvent

group = "dev.nheggoe.boardgame"
version = "1.0.0"

subprojects {

    repositories {
        mavenCentral()
    }

    apply(plugin = "java")

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    }

    dependencies {
        add("testImplementation", platform("org.junit:junit-bom:5.13.4"))
        add("testImplementation", "org.junit.jupiter:junit-jupiter")
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
            )
        }
    }

}
