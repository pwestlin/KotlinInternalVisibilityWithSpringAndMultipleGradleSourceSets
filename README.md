# Kotlin internal visibility with Gradle
Handling Kotlin internal visibility with multiple Gradle sourcesets.

## Motivation
Trying out [modularization](https://en.wikipedia.org/wiki/Modular_programming) in a Spring Boot monolith by using [Kotlin internal visibility modifier](https://kotlinlang.org/docs/visibility-modifiers.html) and multiple sourcesets in [Gradle](https://gradle.org/).

## Implementation

The application is a Spring Boot command line application with four "modules" (Gradle sourcesets):
* `main` is the main starting point of the application.
* `order` has order-specific functionality.
* `inventory` has order-specific functionality.
* `test` unit tests for all other modules.

Module dependencies:

![title](modules.png)

Every module exposes only public classes/functions/other. Internal things are marked `internal`.

### Application inspiration
Heavily inspired by an [example from Spring Modulith](https://github.com/spring-projects/spring-modulith/tree/main/spring-modulith-examples/spring-modulith-example-full). :)

### IntelliJ
IntelliJ has [a bug](https://youtrack.jetbrains.com/issue/KTIJ-7662/IDE-support-internal-visibility-introduced-by-associated-compilations) that makes tests not recognize internal classes/functions/stuff from other sourcesets even if they are configured in Gradle to do just that:
```kotlin
kotlin.target.compilations.filterNot { it.name in listOf("main", "test") }
    .forEach { kotlin.target.compilations.getByName("test").associateWith(it) }
```

The solution, for now, is to suppress the error in _every_ test class...
```kotlin
@Suppress("invisible_reference", "invisible_member")
class DefaultInventoryRepositoryTest {
    ...
}
```