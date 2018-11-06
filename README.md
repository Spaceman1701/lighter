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
This avoids significant amounts of reflection (especially at startup time), allowing Lighter
to (in theory) achieve better performance than other solutions.

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

## Using Lighter

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