val gsonVersion = "2.13.2"

dependencies {
    implementation(project(":core"))

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:${gsonVersion}")
}
