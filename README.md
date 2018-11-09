# Lighter

A lightweight Java microframework for humans

## About This Project

Lighter is a concept Java Web Framework. Somewhat accidentally, it shares a lot of concepts with the
Rocket Web Framework for Rust.

Lighter is designed to offer a high-level of abstraction without high performance costs. It also takes inspiration from
microframeworks by attempting to be agnostic to different configuration decisions. Lighter does not, and __will never__
come with a Dependency Injection solution, a persistence layer, a logging library, or a data serialize/deserialize solution
(though right now, a GSON based serialization/deserialization solution is included - this will be moved to a separate
library at a later data).

Like JAX-RS, Lighter uses annotations to mark routes and other application meta-data. Unlike most
JAX-RS implementations, Lighter uses compile-time processing to wire together the application.
The primary advantage of this is compile-time checking. The Lighter compiler can check for most
configuration and logic errors at compile time. At compile-time, Lighter also has more control regarding the presentation
of errors allowing it to present easy to read and detailed error reports. This means that configuration 
errors will (almost) never cause difficult to understand exceptions at start up. Another advantage to the
compile time approach is that it avoids significant amounts of reflection (especially at startup time), 
allowing Lighter to (in theory) achieve better performance than other solutions.

Generally, Lighter is designed with a paradigm of questioning assumptions about framework 
design. Instead, every feature is designed with the intent of providing a simple, easy to 
understand, and flexible API to users. As such, Lighter does not explicitly implement any JSRs.

Lighter also focuses on making application logic easy to test. Unlike some other frameworks, all of the
data that is passed to a Lighter endpoint is in the form of POJOs. Similarly, endpoints also return
POJOs. There's no need to start a test server or use any complex configuration - testing controllers is as easy 
as testing any other object.

### What is This (And should I use this)?

I decided to roll my own stack for a school project.

Probably don't use it (yet). If I like the direction it's going I might keep maintaining it.

**At best, this is a proof of concept**. Many of the APIs are awkward, inflexible, or downright broken. Test
coverage is... not existent. 

### Goals

- Compile Time Verification
- Fast Startup Times
- Easy Handler Composition

## Advantages of Lighter

1) Compile-time saftey - Lighter can verify application configuration and logic at compile time. This means that a 
Lighter application will never crash with an unreadable stack trace because of a configuration error
2) High performance (and limited reflection) - Since lighter configures your application at compile time, it doesn't
need to do any expensive reflection at runtime
3) Easy debugging and testing - all lighter controllers are POJOs and all endpoints are plain Java methods.
All generated code is human readable. Lighter even follows readable naming conventions - it never gives
generic names like `provider1` or `endpoint123`.
4) Deployment size - Since all of Lighter's complex logic lives in the compiler, deployed binaries only need
a few dependencies. A simple Lighter application is easily less than 8mb (this includes a DI framework, GSON, Log4J2,
and Guava). For comparison, a Spring Boot
"Hello World" application is closer to 20mb.

## Using Lighter

## Configuration through Configuration

Unlike many Java frameworks, Lighter _only_ exposes configuration directly
through a builder object. Lighter will **NEVER** attempt to guess at appropriate
configurations by examining the classpath or files on the disk (unless the application
developer writes their own code to do this). This makes Lighter configuration very
straightforward and predictable.

Lighter also attempts to be _light_ on configuration options. **Lighter enforces it's
conventions**. There is no way to configure around them.

### Annotations

While lighter does not require the use of annotations to define endpoints, it is 
the recommended way to use the framework. 

Lighter's annotations are based on the JAX-RS set of annotations. However, to avoid the 
annotation clutter common in JAX-RS implementations, Lighter has significantly fewer 
annotations. Where JAX-RS specifies 19 annotations, Lighter provides just 8. 

In order to do this, Lighter infers much more data from the signatures of endpoint handler
methods than a JAX-RS implementation. Where JAX-RS requires separate annotations for
specifying the origin of a method parameter (i.e. `HeaderParam`, `PathParam`, `QueryParam`),
Lighter infers origins from the path template. Lighter avoids method-parameter targeting
annotations that hinder readability.

The one exception to this is `Body`, which is used to specify the parameter which corresponds
to the method body.

### Filters (Before and After Handlers)

Lighter does not (and will not) have these. Instead, Lighter uses `RequestGuard`s for
specifying conditions that must be met for a endpoint handler to be invoked and __will__ use
`ResponseConverter`s to handle post-processing to endpoint responses.

(`ResponseConverter` is not yet implemented. In the current pre-MVP version, responses are
hardcoded to send `application/json` content type and convert any response to JSON).

## Plugins

Since Lighter exposes configuration up front, it has no real need for plugins. 
Instead, additional functionality can be provided through normal libraries. For example,
support for Jackson instead of GSON can simply be added through a library that
provides the required adaptor implementations. 

Even more complex features can be added through libraries. Currently, Lighter
has awkward support for Dagger 2. However, this will eventually be resolved
through a Dagger compatibility library that will add code generation that allows
Lighter configuration objects to be generated directly from Dagger modules. Since 
Lighter configuration objects are just `javax.inject` annotated POJOs (which also
follow the Java Beans convention), there's no need to work with Lighter internals
to build this library. Lighter does not even have to know the library exists (as one
would expect). 


## Performance

One of Lighter's goals is improved performance over other frameworks which provide similar levels
of abstraction. Lighter uses compile-time wiring and defaults to the JBoss Undertow webserver 
to achieve this goal. 

While I have yet to do comprehensive benchmarking on any kind, starting the example Lighter app with 4 
routes, Guice dependency injection and Log4j 2 requires about 300-400 ms on a 2017 MacBook Pro. Starting
the example Spring-Boot application requires 3.5-4.5 seconds. This represents an order of magnitude 
faster start-up times.

In this test, creating the Guice Injector object required between 200-250ms. Upcoming support for Dagger 2 
or hand-written dependency injection could significantly reduce startup times. Basic testing using Dagger 2
has shown start-up times for the whole application are closer to ~150ms.

These start-up times make Lighter viable in use-cases where fast deployment is required (such as 
fast horizontal scale-out situations)