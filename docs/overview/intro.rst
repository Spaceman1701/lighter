Introduction
============

Lighter is a fully-featured web framework for Java. It's primarily designed 
for RESTful services, but it can be used for all sorts of other things too!

Lighter is different than most other Java web frameworks. Lighter stands inbetween
micro-frameworks like Spark Java and giant monolithic frameworks like Spring Boot. Like
a micro-framework, Lighter is small and doesn't come with a lot of dependencies. It lets
you choose your own serialization, persistance, and dependency injection solutions. Like
a monolithic framework, Lighter provides declarative configuration and high-level abstractions.

Lighter achieves this by working at *compile-time instead of run-time*. Lighter uses almost no
reflection. Instead, it depends on annotation processors to provide high level constructs. This
allows Lighter's abstractions to have close to zero cost.

Lighter's goals:

#. Easy to use. Lighter's APIs should be intuitive and straight forward. Behavior should be easy to reason about. 

#. Safe. Lighter should find errors at compile-time, not run-time. Lighter should take advantge of Java's type system to provide type safe interfaces.

#. Performant. Lighter should start-up quickly and incur little overhead at runtime.

#. Testable. Lighter applications should be easy to test. Lighter should not force applications to use complex test constructs just to run unit-tests.

#. Modular. Lighter should not make decisions for the application. It should be easy to switch out components as required. Lighter shouldn't pull in 10s of megabytes of dependencies.

This documentation is intended to provide details about how to use Lighter and about how Lighter
works. For API reference, refer to the ``javadoc``.
