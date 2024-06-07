actor Main
  new create(env: Env) =>
    env.out.print("Hello, world!")
    env.out.print("Goodbye, world!")
