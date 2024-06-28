# Kotlin internal visibility with Gradle
Handling Kotlin internal visibility with multiple Gradle sourcesets.

## Motivation
Trying out [modularization](https://en.wikipedia.org/wiki/Modular_programming) in a Spring Boot monolith by using [Kotlin internal visibility modifier](https://kotlinlang.org/docs/visibility-modifiers.html) and multiple sourcesets in [Gradle](https://gradle.org/).


## Inspiration
Heavily inspired by an [example from Spring Modulith](https://github.com/spring-projects/spring-modulith/tree/main/spring-modulith-examples/spring-modulith-example-full). :)

## IntelliJ
IntelliJ has [a bug](https://youtrack.jetbrains.com/issue/KTIJ-7662/IDE-support-internal-visibility-introduced-by-associated-compilations) so that tests cannot recognize internal classes/functions/stuff from other sourcesets even if they are configured to do so in Gradle:
```kotlin
```