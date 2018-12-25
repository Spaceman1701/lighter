Injection API
=============
Control how your classes get instantiated by Lighter. The primary class applications
will interact with is the ``InjectionObjectFactory``. This functional interface is designed
to provide a implementation agnostic API for dependency injection containers. The interface is
very simple as it is only used when Lighter needs to construct a class for the application. 

The interface is designed to match the Guice ``Injector#newInstance`` method.

The other class used for dependency construction is the ``ReverseInjector``. Implementations
of ``ReverseInjector`` provide an instance of ``InjectionObjectFactory``. Lighter will automatically
generate an implementation of ``ReverseInjector`` that has a setter for every dependency Lighter will
need to construct. The auto generated implementation conforms to Java Beans and javax.Inject standards
for dependency Injection. This implementation can be used as a configuration bean with dependency injection
frameworks that do not have an Injector class (like Dagger 2).