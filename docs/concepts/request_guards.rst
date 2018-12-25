Request Guards
==============

Request guards allow your application to define preconditions to endpoint execution. This
feature is inspired by one of Ligther's primary inspirations: `Rocket Web Framework`_.

.. note:: This feature is currently in early stages of development. Expect lots of changes.

.. _Rocket Web Framework: https://rocket.rs/

Request Guards are special endpoint method dependencies that are not constructed directly
from the request. Instead, Request Guards are constructed by application defined logic 
using a ``RequestGuardFactory``. Request Gaurds are identified using the ``RequestGuard``
marker interface. RequestGuardFactories are identified using the ``RequestGuardFactory`` interface
and the ``@ProducesRequestGuard`` annotation.

.. note:: The RequestGuardFactory API is an area that is targeted for change in the future. It is very
    awkward to require both an interface and annotation to mark RequestGuardFactories.

Since Request Guards are constructed by application logic, they can be used to define custom pre-requesite
conditions for endpoints. In order to use a Request Guard, the endpoint method must simply add a parameter
of a ``RequestGuard`` type. Lighter will determine how to fulfill that requirement at compile time.

.. note:: Currently, Lighter does not support Optional Request Guards. This feature will be added soon.

.. note:: Request guard errors current cause a ``500 - Internal Server Error``. In the future, the API will
    allow more control over how Request Guard construction errors occur.

RequestGuards are the idiomatic way to implement authentication and other cross-cutting concerns.