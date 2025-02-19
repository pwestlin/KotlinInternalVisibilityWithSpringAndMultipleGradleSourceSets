plugins {
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    "2.1.10".let { kotlinVersion ->
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
    }
    idea
}

group = "nu.westlin.gradle"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

private val orderSrcSet = "order"
private val inventorySrcSet = "inventory"
private val statisticsSrcSet = "statistics"

sourceSets {
    create(orderSrcSet)
    create(inventorySrcSet)
    create(statisticsSrcSet)
}

// "Group" for spring-boot-starter so that they can be imported to other sourcesets than main.
fun DependencyHandler.springBootStarterDependency(configurationName: String) {
    add(configurationName, "org.springframework.boot:spring-boot-starter")
}

dependencies {
    springBootStarterDependency("implementation")
    //implementation()
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(sourceSets.named(orderSrcSet).get().output)

    // Needed for Spring so it can find Spring beans in inventorySrcSet.
    runtimeOnly(sourceSets.named(inventorySrcSet).get().output)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    springBootStarterDependency("${orderSrcSet}Implementation")
    springBootStarterDependency("${inventorySrcSet}Implementation")
    springBootStarterDependency("${statisticsSrcSet}Implementation")

    "${inventorySrcSet}Implementation"(sourceSets.named(orderSrcSet).get().output)
    "${inventorySrcSet}Implementation"(sourceSets.named(statisticsSrcSet).get().output)

    "${orderSrcSet}Implementation"(sourceSets.named(statisticsSrcSet).get().output)
}

// Tests should recognize internal classes/functions/stuff from other modules.
kotlin.target.compilations
    .filterNot { it.name in listOf("main", "test") }
    .forEach { kotlin.target.compilations.getByName("test").associateWith(it) }

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

/**
 * Runs unit tests and prints a report.
 */
tasks.withType<Test> {
    useJUnitPlatform()

    /*
     Skapar en snygg "testrapport" i konsolen.
     */
    addTestListener(
        object : TestListener {
            override fun beforeTest(p0: TestDescriptor?) = Unit
            override fun beforeSuite(p0: TestDescriptor?) = Unit
            override fun afterTest(desc: TestDescriptor, result: TestResult) = Unit
            override fun afterSuite(desc: TestDescriptor, result: TestResult) {
                if (desc.parent == null) {
                    val output =
                        "${desc.name} results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
                    val startItem = "|  "
                    val endItem = "  |"
                    val repeatLength = startItem.length + output.length + endItem.length
                    println(
                        "\n" + ("-".repeat(repeatLength)) + "\n" + startItem + output + endItem + "\n" + ("-".repeat(
                            repeatLength
                        ))
                    )
                }
            }
        }
    )
}


/**
 * Task printing source code locations, outputs och dependencies for all SourceSets.
 */
tasks.register("printSourceSetInformation") {
    doLast {
        fun asRelativePath(file: File): String {
            return if (file.absolutePath.startsWith(project.projectDir.absolutePath)) {
                file.relativeTo(project.projectDir).toString()
            } else {
                file.name
            }
        }

        val indentation = "   "
        sourceSets.forEach { srcSet ->
            println("[${srcSet.name}]")
            println("Source directories:")
            srcSet.allJava.srcDirs.forEach { file ->
                println("${indentation}${file.relativeTo(project.projectDir)}")
            }
            println("Output directories:")
            srcSet.output.classesDirs.files.forEach { file ->
                println("${indentation}${file.relativeTo(project.projectDir)}")
            }
            println("Compile classpath:")
            srcSet.compileClasspath.forEach { file ->
                println("${indentation}${asRelativePath(file)}")
            }
            println()
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
