Introduction
============

This section will go into detail about Lighter's design and architecture.

Lighter's goals:

#. Easy to use. Lighter's APIs should be intuitive and straight forward. Behavior should be easy to reason about. 

#. Safe. Lighter should find errors at compile-time, not run-time. Lighter should take advantge of Java's type system to provide type safe interfaces.

#. Performant. Lighter should start-up quickly and incur little overhead at runtime.

#. Testable. Lighter applications should be easy to test. Lighter should not force applications to use complex test constructs just to run unit-tests.

#. Modular. Lighter should not make decisions for the application. It should be easy to switch out components as required. Lighter shouldn't pull in 10s of megabytes of dependencies.

This documentation is intended to provide details about how to use Lighter and about how Lighter
works. For API reference, refer to the ``javadoc``.

.. note:: *(from the author, Spaceman1701)*

    Currently, Lighter's Minimum Viable Product version is available. This version is designed to demonstrate the 
    feasability of Lighter. However, it does not have the final feature set or the final APIs. Many of the APIs are
    awkward to use or broken.

    With this said, Lighter's MVP version works quite well. It performs very well when compared to Spring Boot and
    is usable for real-world applications. I've decided I'm going to keep working on Lighter. I'll be using it for
    personal projects whenever I can. I'll also continue to improve the feature set and APIs. I hope that at some
    point I'll be able to consider Lighter as more than a proof of concept.

    If somehow you've come accross these docs and you want to contribute to Lighter, head over to GitHub_. I'd love
    contributions. As I move forward, I'll be tracking features and issues on GitHub.

.. _GitHub: https://github.com/Spaceman1701/lighter
