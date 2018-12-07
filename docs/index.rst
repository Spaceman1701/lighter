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
