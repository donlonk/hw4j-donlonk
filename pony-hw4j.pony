actor Main
    new create(env: Env) =>
        let pony = "🐎"
        env.out.print(pony)

        let x: I32 = 42
        let y: I32 = 13
        env.out.print((x + y).string())

        let z: Bool = true
        env.out.print(z.string())