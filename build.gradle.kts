plugins {
    id("java")
    id("application")
    id("checkstyle")
    id("jacoco")
}

group = "nu.csse.sqe"
version = "1.0"

application {
    mainClass = "ui.Main"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.easymock:easymock:5.4.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
    systemProperty("java.awt.headless", "true")
    finalizedBy(tasks.jacocoTestReport)
}

// The rubric exempts views, Main, and enums from the coverage and mutation targets.
val untestedClasses = listOf(
    "ui/*View*",
    "ui/GameFrame*",
    "ui/Main*",
    "domain/piece/PieceColor*",
    "domain/piece/PieceType*",
)

val coveredClasses = sourceSets.main.get().output.asFileTree.matching {
    include("**/*.class")
    exclude(untestedClasses.map { "$it.class" })
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(coveredClasses)
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    classDirectories.setFrom(coveredClasses)
    violationRules {
        rule {
            limit {
                counter = "BRANCH"
                minimum = "1.0".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

// Google's method-name pattern for production code. Test sources also allow the
// underscores of Method_State_ExpectedBehavior; the TestMethodName check in the config
// requires that form on every @Test method.
val productionMethodName = "^[a-z][a-z0-9]\\w*$"
val testMethodName = "^[a-zA-Z][a-zA-Z0-9_]*$"

checkstyle {
    toolVersion = "10.12.5"
    configFile = file("config/checkstyle/checkstyle.xml")
    maxWarnings = 0
    configProperties = mapOf("methodNameFormat" to productionMethodName)
}

tasks.named<Checkstyle>("checkstyleTest") {
    configProperties = mapOf("methodNameFormat" to testMethodName)
}
