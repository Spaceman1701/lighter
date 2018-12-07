Lighter Architecture
====================

Basics
------

Lighter is built of two libraries: The runtime library, ``lighter-core``, and
the compile-time library, ``lighter-compiler``.

Lighter core provides the the stock JBoss Undertow implementations of the core Lighter APIs. It also
defines the declarative annotation API.

Lighter compiler consumes the declarative annotation API. It is responsible for providing compile-time
verification and for generating application-speicifc implementations for Lighter's abstractions. The
compiler uses compile-time reification_ of the application to do verification and code generating.

.. _reification: https://en.wikipedia.org/wiki/Reification_(computer_science)

Terminology
-----------

Some important terms for the rest of this documentation.

Lighter
    The Lighter Web Framework. This will be used to mean the framework as a whole (as opposed to individual components).'

The Application
    The actual application that is built using Lighter. This term is used to mean "any application" as opposed to 
    refering to a speicifc one. The Application consumes Lighter.

Application Developer
    The developer who is using Lighter to implement her application.

Lighter Core (the Core)
    The Lighter runtime library and APIs. The stock implementation is ``lighter-core``. When used in this documentation, 
    it usually refers to the stock implementation.

Lighter Compiler (the Compiler)
    The Lighter compile-time library. The stock implementation is ``lighter-compiler``. For what it's worth, implementations
    of Lighter Compiler do not necessarily need to be compile-time only. Essentially, the Lighter Compiler is an 
    invisible provider for the implementations of Lighter's high-level abstractions. Since the Compiler is invisible to the
    application, it's implementation ins't necessarily important. In the future, reflection-based implementations or byte-code
    weaving-based implementations might be possible. When used in this documentation, it usually refers to the stock implementation.

Lighter Backend
    The runtime-level implementation of Lighter. This refers to any Lighter APIs that are implemented by runtime code. The 
    most significant component of the Lighter backend is the web server.

Interop Between Lighter Core and Compiler
-----------------------------------------

Both libraries are built as seperate entities. In fact, it's possible to use Lighter Core without using
Lighter Compiler. However, many of the high-level abstractions that make Lighter pleasent to use are
not available without the compiler. Since the application only depends on Lighter Core, this decoupling
means that it's possible for different implementations of Lighter Compiler to be used without changing
the application.

Since Lighter is currently in MVP, the actual interop between the Core and Compiler components is very limited.
However, future versions of Lighter will have a well-defined API for both components to use. This formalization
of the API has a couple benifits.

#. Improved versioning. It would be possible to version the Core and Compiler components independantly.
#. Support for application libraries. A Core-Compiler API would be able to provide functionality for already-compiled applications to be used as libraries
#. Multiple Compiler implementations. This one's obivious. It'd be interesting to see reflection-based implementations in the future, for example.

Compiler Components
-------------------

The Lighter is iterative. Most iterations do some form of reification_ on The Application's 
code. Other iterations attempt to verify that some set of invariants hold in the reified model. As such,
it makes sense to break the compiler into components defined by which reified model they use.

* Annotation Model Components - a lightweight model which represents the locations and data of each Lighter annotation in The Application
    - Annotation Validators - validation that annotations are placed correctly and do not have data errors
    - Model Builder - uses the Annotation model to build a more detailed model
* Application Model - a detailed reified model of The Application's structure
    - Model Validators - validation that the model represents a legal application that will work at runtime
    - Dependency Collection - collect all of the non-Lighter classes that are required by the model
    - Request Guard Collection - collects request guards that might be used by The Application
    - Controller Generation - code generation for HTTP endpoint controllers. Produces a new model based on the generated code
* Generated Code Model - reified model of the application refering to actual generated objects
    - Reverse Injection Generation - generate an injector for handling application depedencies
    - Route Configuration Generation - generate code for configuration for using generated endpoint handlers

To aid with each of these components, the Compiler also contains components for managing and reporting erros, generating
dynamic code, and defining compilation steps.

Currently, the "Generated Code Model" lacks proper definition in the actual implementation of Lighter Compiler. Cleaning up 
the compiler code will be a major focus in future versions of Lighter.

The Lighter Compiler is very complex. More detail about each of these major components will be provided further in the documentation.

Core Components
---------------

Lighter's APIs can be subdivided into a couple catagories. 

* Declarative - annotations used to identify components of the application.
* Request and Response - used to construct and represent HTTP requests and responses
* TypeAdapter - pluggable API for defining serialization and deserialization procedures
* Injection - pluggable API for depedency management
* Autoconfig - API for using configuration generated by Lighter Compiler.

Lighter Core also provides the backend implementation. The stock Lighter Core implementation provides a backend
which uses JBoss Undertow as a web server.
