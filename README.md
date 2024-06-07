# hw4j-donlonk

# Running the Program

1. Open a terminal and navigate to the project directory.

2. Use Gradle to run the interpreter with the example Pony file provided:

    ./gradlew run --args="/path/to/your/pony-hw4j.pony"

    Replace `/path/to/your/pony-hw4j.pony` with the actual path to the Pony source file you want to interpret.

# Example Pony Program

Here's an example of a Pony program that the interpreter can execute:

```pony
actor Main
  new create(env: Env) =>
    let pony = "🐎"
    env.out.print(pony)

    let x: I32 = 42
    let y: I32 = 13
    env.out.print((x + y).string())

    let z: Bool = true
    env.out.print(z.string())

# Error
@donlonk ➜ /workspaces/hw4j-donlonk (main) $ ./gradlew run --args="/workspaces/hw4j-donlonk/pony-hw4j.pony"
Path for java installation '/usr/lib/jvm/openjdk-17' (Common Linux Locations) does not contain a java executable

> Task :app:run
Error at token: COLON : null - Expect expression.
Error at token: RIGHT_PAREN ) null - Expect expression.
Error at token: EQUAL = null - Expect expression.
Error at token: GREATER > null - Expect expression.
Error at token: EQUAL = null - Expect ':' after variable name.
Error at token: DOT . null - Expect expression.
Error at token: PRINT print null - Expect expression.
Error at token: DOT . null - Expect expression.
Error at token: PRINT print null - Expect expression.
Error at token: DOT . null - Expect expression.
Error at token: PRINT print null - Expect expression.
Statements: [edu.sou.cs452.hw3j.Stmt$Expression@372f7a8d, edu.sou.cs452.hw3j.Stmt$Expression@2f92e0f4, edu.sou.cs452.hw3j.Stmt$Expression@28a418fc, edu.sou.cs452.hw3j.Stmt$Expression@5305068a, edu.sou.cs452.hw3j.Stmt$Expression@1f32e575, edu.sou.cs452.hw3j.Stmt$Var@279f2327, edu.sou.cs452.hw3j.Stmt$Var@2ff4acd0, edu.sou.cs452.hw3j.Stmt$Expression@54bedef2, edu.sou.cs452.hw3j.Stmt$Var@5caf905d, edu.sou.cs452.hw3j.Stmt$Expression@27716f4]
Runtime error: Invalid operands for dot operator.

BUILD SUCCESSFUL in 1s
2 actionable tasks: 2 executed

It looks like the program is expecting an expression after alomost every symbol which is not right and I am not sure how to fix it.
