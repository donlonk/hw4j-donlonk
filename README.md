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
 > Task :app:run FAILED
Unexpected character: >
Error at (: Expect 'env' as parameter.
Error at env: Expect expression.
Error at :: Expect expression.
Error at Env: Expect expression.
Error at ): Expect expression.
Error at =: Expect expression.
Error at =: Expect ':' after variable name.
Error at env: Expect expression.
Error at .: Expect expression.
Error at out: Expect expression.
Error at .: Expect expression.
Error at pony: Expect expression.
Error at ): Expect expression.
Error at env: Expect expression.
Error at .: Expect expression.
Error at out: Expect expression.
Error at .: Expect expression.
Error at x: Expect expression.
Error at +: Expect expression.
Error at y: Expect expression.
Error at ): Expect expression.
Error at .: Expect expression.
Error at string: Expect expression.
Error at ): Expect expression.
Error at ): Expect expression.
Error at env: Expect expression.
Error at .: Expect expression.
Error at out: Expect expression.
Error at .: Expect expression.
Error at z: Expect expression.
Error at .: Expect expression.
Error at string: Expect expression.
Error at ): Expect expression.
Error at ): Expect expression.
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "edu.sou.cs452.hw3j.Stmt.accept(edu.sou.cs452.hw3j.Stmt$Visitor)" because "stmt" is null
        at edu.sou.cs452.hw3j.Interpreter.execute(Interpreter.java:20)
        at edu.sou.cs452.hw3j.Interpreter.interpret(Interpreter.java:12)
        at edu.sou.cs452.hw3j.App.run(App.java:56)
        at edu.sou.cs452.hw3j.App.runFile(App.java:28)
        at edu.sou.cs452.hw3j.App.main(App.java:20)

It looks like the program is expecting an expression after alomost every symbol which is not right and I am not sure how to fix it.
