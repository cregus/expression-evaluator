# Expression Evaluator [![](https://jitpack.io/v/cregus/expression-evaluator.svg)](https://jitpack.io/#cregus/expression-evaluator)
Library written in Kotlin to help evaluate mathematical expressions that can be extended by custom operators and functions.

## Setup
### Add the JitPack repository to your build file
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

### Add library dependency to your project build file
```
dependencies {
  implementation 'com.github.cregus:expression-evaluator:1.0.1'
}
```

## Sample usage
### Simple expressions.
```kotlin
println(evaluate("2+2")) // Prints 4
```

### Using functions.
```kotlin
println(evaluate("round(10/3,2)")) // Prints 3.33
```

### Using variables.
```kotlin
println(evaluate("x*y", data = mapOf("x" to 2, "y" to 3))) // Prints 6
```

### Defining custom functions.
```kotlin
Tokenizer.registerFunction("random") { a, b -> Random.nextInt(a.toInt(), b.toInt()).toBigDecimal() }
println(evaluate("random(1,6)")) // Prints random number between 1 (inclusive) and 6 (exclusive)
```