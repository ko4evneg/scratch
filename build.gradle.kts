plugins {
    id("java")
}

group = "com.github.ko4evneg"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.38")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")

    annotationProcessor("org.projectlombok:lombok:1.18.38")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
