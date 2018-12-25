Automatic Configuration
=======================

Control how generated code is used. Autoconfiguration can be accessed using the ``AutoConfigurationFactory``. 
This singleton factory class can be used to access the configuration objects that Lighter generates at compile
time. Normally, applications will access this class to load the route configuration instance to pass to ``Lighter.Builder``.