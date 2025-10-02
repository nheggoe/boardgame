import org.gradle.api.tasks.testing.logging.TestLogEvent

group = "dev.nheggoe.boardgame"
version = "1.0.0"

subprojects {

    val junitVersion = "6.0.0"
    val assertjVersion = "3.27.6"
    val mockitoVersion = "5.20.0"

    repositories {
        mavenCentral()
    }

    apply(plugin = "java")

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    }

    dependencies {
        add("testImplementation", platform("org.junit:junit-bom:$junitVersion"))
        add("testImplementation", "org.junit.jupiter:junit-jupiter")
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        add("testImplementation", "org.assertj:assertj-core:$assertjVersion")
        add("testImplementation", "org.mockito:mockito-core:$mockitoVersion")
        add("testImplementation", "org.mockito:mockito-junit-jupiter:$mockitoVersion")
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
