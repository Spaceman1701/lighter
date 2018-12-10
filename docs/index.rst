.. Lighter documentation master file, created by
   sphinx-quickstart on Tue Dec  4 21:36:23 2018.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Lighter Web Framework Documentation
===================================

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

Lighter aims to be the anti-framework framework. Whenever possible, it achieves abstraction without
magic. When it does use magic, Lighter focuses on making it inutitive and easy to follow. Lighter avoids
pulling in dependencies it doesn't have to and lets the developer structure their application. Lighter provides
abstraction without incuring performance penalties or clarity costs.

For complete API documentation, check out the javadocs (link TBD).

**TESE DOCS ARE STILL A WORK IN PROGRESS.**
Almost every page still has a lot of work that needs to be done. Many pages have not been
started. 

.. toctree::
   :maxdepth: 2
   :caption: Overview

   overview/intro.rst
   overview/architecture.rst

.. toctree::
    :maxdepth: 2
    :caption: Getting Started

    starting/intro.rst

.. toctree::
    :maxdepth: 2
    :caption: Concepts

    concepts/intro.rst
    concepts/endpoints.rst
    concepts/response_api.rst
    concepts/request_guards.rst
    concepts/injection.rst
    concepts/lighter.rst
    concepts/autoconfig.rst
