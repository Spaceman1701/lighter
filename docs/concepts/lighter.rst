Lighter API
===========
Construct and interact with the ``Lighter`` instance. The ``Lighter`` object represents the 
application itself. Lighter instances can only be constructed using the ``Lighter.Builder`` API.
This fluent API provides many configuration options for Lighter.

Both ``Lighter`` and ``Lighter.Builder`` are interfaces which define what configuration options and
operations all Lighter backends must support. Backends can choose to implement extra operations. The
Undertow backend (which is currently the only backend), provides only the required methods.

Lighter runs asycronously. ``Lighter#start`` returns as soon as the server is started. This allows the main
thread to be used for controlling Lighter.