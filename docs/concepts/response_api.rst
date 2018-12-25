Response API 
============

The response API allows your application to return data to the outside world. Since every endpoint
must return a response, the API is designed to be very concise. However, applications will have
extemely variable requirements for Response structure, so the API also allows a great deal of
flexibility. In addition to this, Responses must be easy to use in unit tests.

The main class that applications will interact with is the ``Reponse`` class. To the user, ``Response`` is
a Plain Old Java Object. ``Response`` is immutable and method calls have no side effects. In addition to
``Response``, applications will interact with instances of the ``ResponseDecorator`` functional interface.
the ``Response#with`` method provides a fluent API for adding decorators to the ``Response`` object. This 
is the primary way to build custom responses.

Lighter also provides the ``Responses`` static factory class with utility methods for constructing common HTTP 
response types. ``Responses`` has methods for constructing ``3xx - Redirect``, JSON content, and no content
responses. 

The Response API is type safe. The ``Response`` class type parameter is used to represent the type of the
response body content. ``ResponseDecorator`` application can change the type parameter. This allows chains
of decorator application to maintain type safety. ``java.lang.Void`` is used to represent an empty response.

Using Response
--------------

The ``Response`` class does not contain the serialized data. Instead, it contains a reference to the Java
object that will be serialized. Lighter uses the top level ``TypeAdapterFactory`` to serialize the content.
Lighter ensures that the type is serialized with the correct MIME Media Type by reading the Content-Type header
on the response.

When using the ``Response`` for unit testing endpoints, the Java object is directly available.

Standard Response Decorators
----------------------------

Lighter provides a few standard response decorators. These allow most required responses to be constructed.
Since ``ResponseDecorator`` is a functional interface, lambda functions can also be used.

The provided decorators are:

* HeaderResponse - adds a header to the response
* StatusResponse - sets the response status code
* JsonContent - adds an object as the response body and sets the content-type header to ``application/json``