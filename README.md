# scalaz-kleisliio

[![Gitter](https://badges.gitter.im/scalaz/scalaz-kleisliio.svg)](https://gitter.im/scalaz/scalaz-kleisliio?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Goal
High-performance arrow based library which makes pure and impure code to coexist in a purely-functional way.

## Introduction & Highlights
Using ***Kleisli*** `A => F[B]` as arrow abstraction and ***ZIO*** as a type `A => IO[B]`, **KleisliIO** allows to integrate impure code in a separate constructor `A => B` with no additional IO overhead. This constructor allows fusion of core arrow operations when both left and right side are impure functions.

Critical sections of code with **impure functions** will benefit from KleisliIO because of the **dramatic reduction in overhead**.

* Type-safe, purely-functional.
* Impure code integration.
* Comprehensive, composable

## Competitors
[traneio/Arrows](https://github.com/traneio/arrows)
1. Purely-functional: Y
2. High-performance: Y
3. Performance optimisation on impure sections of code: N
4. Wide range of combinators: N

## Background
[Generalising Monads to Arrows](http://www.cse.chalmers.se/~rjmh/Papers/arrows.pdf)
[traneio/Arrows](https://github.com/traneio/arrows)