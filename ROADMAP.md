# Scalaz KleisliIO Development Plan
# Introduction
John already implemented a [first version](https://github.com/scalaz/scalaz/issues/1857) of the library in **ZIO project** which contains:

* `KleiliIO` definition and implementation of `Pure` and `Impure`
* Basic **combinators** presented in his [Blazing Fast, Pure Effects without Monads](https://www.slideshare.net/jdegoes/blazing-fast-pure-effects-without-monads-lambdaconf-2018) presentation at *LambdaConf 2018*
* **Tests** (not high test coverage though)
* Two **benchmarks** (array fill & bubble sort) in which he compares *KleisliIO* vs *Monix Task* vs *Cats IO*

# Phase 0: get familiar with the API
Before starting writing or thinking about anything it is **very important** to get familiar with the existing code.

So first steps would be:
1. Fork `ZIO project` and check John's code
2. Add your own benchmark so that you get familiar with `KleisliIO` and its competitors' API

# Phase 1: Extending API
John's implementation is functional but basic so we need first to extend it so that `KleisliIO` has a more that good starting point.

To-Do's:

* **Add more combinators**: take a look on *Haskell's Kleisli*, other `Scalaz`'s types
* **Provide with instances**: implement basic instances like `Functor`, `Apply`, etc... in *Scalaz*.
* **Improve error handling / recover from error**
* **Make it support recursion**
* **Build more tests for the existing implementations**

# Phase 2: Scala integration
Since *Scala* does not provide an out-of-the-box support for arrows we have to make things easier for the developers who wish to work with `KleisliIO`.
The main purpose is to make the composition similar to what [*Haskell*](https://www.haskell.org/arrows/syntax.html) does by achieving one of the following to-do's:

* **Creating some cool grammar**
* **Developing a plugin to facilitate combination**
* **Extend Scala compiler to support arrows in for-comprehensions**

# Phase 3: Improve performance
To-Do's:

* **Memory allocation** can still be reduced to 0 in places where `Tuple` or `Either` is used by using own classes.
* **KleisliIO.Impure can have a *secretRun* with a better performance**

# Phase 4: New effectful type Trio[+E, -R, +A]