plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    "2.0.0".let { kotlinVersion ->
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

sourceSets {
    create(orderSrcSet)
    create(inventorySrcSet)
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

    // Denna behövs för att Spring ska förstå att det finns springbönor i modulen inventorySrcSet.
    runtimeOnly(sourceSets.named(inventorySrcSet).get().output)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    springBootStarterDependency("${orderSrcSet}Implementation")

    //"${orderSrcSet}Implementation"("org.springframework.boot:spring-boot-starter")

    //"${inventorySrcSet}Implementation"("org.springframework.boot:spring-boot-starter")
    springBootStarterDependency("${inventorySrcSet}Implementation")
    "${inventorySrcSet}Implementation"(sourceSets.named(orderSrcSet).get().output)
}

// För att "test" ska få åtkomst till de saker som är märkta "internal" i andra sourcesets
//kotlin.target.compilations.getByName("test").associateWith(kotlin.target.compilations.getByName(orderSrcSet))
// Tests should recognize internal classes/functions/stuff from other modules.
kotlin.target.compilations.filterNot { it.name in listOf("main", "test") }.forEach { kotlin.target.compilations.getByName("test").associateWith(it) }

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

/**
 * Kör enhetstester.
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
 * Task för att skriva ut källkod, outputs och dependencies för alla SourceSets.
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
