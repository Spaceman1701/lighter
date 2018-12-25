Endpoints
=========

Endpoints are the core of any Lighter application. They allow the application to interact 
with the outside world. In Lighter, endpoints are methods that are identified using an endpoint annotation.
In the current MVP version of Lighter the endpoint annotations are ``@Get``, ``@Post``, 
``@Put``, and ``@Delete``. Each of these annotations corresponds to an HTTP method. 

Endpoints must always return a ``Response``. See the *Response API* docs page for details
about constructing responses. 

Endpoint Annotations
--------------------

All of the endpoint annotations have the same API. Each one has an optional ``value`` field which can 
be used to define a path template stub that the endpoint method should respond to. The full path template that
defines the endpoint is constructed by prepending the endpoint's Resource Controller path stub to the stub provided
in the endpoint annotation. See more about this in the Resource Contollers section below.

In order to handle HTTP requests, Lighter matches the request method and path against the set of endpoint methods
and path templates in the application. Method parameters are fulfilled by path parameters, query parameters, and the
request body.

Resource Controllers
--------------------

Every endpoint method must be a member of a ``@ResourceController`` annotated class. Resource Controllers are plain Java classes.
Resource Controllers must specify a path template stub that will be prepended to all of their members. This is useful as it avoids
the necessity of rewriting parts of a the template multiple times for related endpoints. 

Resource Controllers will be instaintiated by Lighter. Thus, they must be instaintiable by the ``InjectionObjectFactory``. See the docs
page on the Injection API for details.

Path Template Syntax
--------------------

Path template syntax is similar to other web frameworks. Templates can contains three types of components: 
Normal, Parameter, and Wildcard. Normal components match components exactly equal to themsevles. A path template
made of only Normal components would match only paths that are identical to it. Parmaeter components will match anything and
bind it to the provided name. Parmaeters are denoted by surrounding a name with ``{`` and ``}``. Every parameter 
as a type which is inferred from the method signature. Wildcard components are denoted by a ``*`` and greedly match 
any number of components. 

Here are some examples: 

The template ``foo/bar/123`` will match
    exactly the path ``foo/bar/123`` and nothing else.

The template ``foo/bar/{id}`` will match
    any path with exactly 3 components that begins with ``foo/bar/``. The third component of
    the path will be bound to the name "id".

The template ``foo/bar/*`` will match
    any path that begins with ``foo/bar/``.

The template ``foo/*/bar`` will match
    any path that begins with ``foo`` and ends with ``bar``


Query Parameters
----------------

HTTP query parameter bindinds can be specified in a similar way to path Parmaeter components. However, query parameters
do not appear as part of the path template. Instead, the ``@QueryParameters`` annotation is used to provide a list of
name bindings. Since the names of query parameters are exposed as part of the applications API, Lighter allows external
and internal names of query parameters to differ. The external (*exposed*) name is what HTTP calls should use. The internal
(*mapped*) name should match the name of the parameter on the Java endpoint method. 

Query parmaeter names are specified using an array of Strings. Exposed names and mapped names are seperated by a ``:``.
If only one name is provided, Lighter assumes the exposed name is identical to the mapped name.

Here are some examples:

The parameter ``foo:bar`` specifies
    an exposed name ``foo`` which maps to a parameter on the Java method named ``bar``

The parameter ``foo`` specifies
    an exposed name ``foo`` which maps to a parmaeter on the Java method named ``foo``

Similar to path Parameters, query parameter types are inferred from the Java method.

Accessing the Request Body
--------------------------

The request body content can be mapped to any method parameter by annotating it with ``@Body``. The type of
the body content is infered from the method.

Parameter Type Inference
------------------------

All endpoint parameter types are inferred from the Java method signature. Any Java type can be used as long
as the application ``TypeAdapterFactory`` is capable of producing a ``TypeAdater`` for the type. Query and
path parameters are assumed to have a MIME Media Type of ``text/plain``. The Media Type of the request body
is determined by the Content-Type header.

If a method parameter is optional (i.e. an error should not occur if Lighter can not provide data for the parameter),
it should have a type of ``java.util.Optional`` (or one of the allied Optional types provided in the standard library). Since
Lighter performs type inference at compile time, it is able to use the generic type parameter of ``Optional`` for serialization
and deserialization logic.

Lighter **will never** provide a ``null`` value for a method parameter. If a non-Optional parameter can not be provided for
any reason, Lighter will throw an error.