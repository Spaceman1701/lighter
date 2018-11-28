package fun.connor.lighter;


import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.autoconfig.RouteConfiguration;
import fun.connor.lighter.injection.InjectionObjectFactory;

/**
 * The Lighter inversion of control container. This class and it's associated builder are used
 * for construction and interaction with the Lighter application server. Since Lighter behaves as a
 * lightweight inversion of control system, this consists mostly of configuring the Lighter instance
 * using the builder.
 * <br>
 * All methods on the Lighter object are non-blocking. This allows the application developer to maintain flow
 * control on the main thread. Lighter initializes it's own threads for running the backend webserver and the
 * application defined endpoints.
 * <br>
 * The Lighter instance itself does not use compile time generated code (or perform any classpath scan or
 * reflection). Instead, Lighter uses plain Java interfaces to represent different forms of configuration. These
 * can be written with different implementations for different application specific use cases. Lighter does come
 * with automatic configuring implementations of these objects which are sufficent for most general use cases. For
 * more details, see {@link fun.connor.lighter.autoconfig.AutoConfigFactory}.
 */
public interface Lighter {
    /**
     * Start the Lighter application server. This method will return immediately after the server
     * has been started. This method causes Lighter to start its own pool of threads which will
     * immediately start serving requests.
     */
    void start();

    /**
     * Stop the Lighter application server. Ends all background threads that the container may be
     * maintaining
     */
    void stop();

    /**
     * The generic Builder interface for Lighter objects. Contains methods which all Lighter builder's must have.
     * These methods must be shared by all Lighter implementations as they will represent configuration options that can
     * be requested by endpoints.
     */
    interface Builder {
        /**
         * Add all the routes from the given route configuration object. This method can be called
         * multiple times on a builder. This method is currently one of the few methods where runtime
         * exceptions can occur during Lighter configuration. If the added RouteConfiguration instance
         * contains routes that already exist, this method may throw a {@link IllegalArgumentException}
         * @param configuration The route configuration to add
         * @return self
         */
        Builder addRouter(RouteConfiguration configuration);

        /**
         * Add an injection factory representing the application's dependency injection solution. This method
         * should only be called once.
         * @param factory the factory to add
         * @return self
         */
        Builder injectionFactory(InjectionObjectFactory factory);

        /**
         * Add a type adaptor factory for serialization and deserialization of data to Java objects. The factory
         * added must be sufficient for all types and media types the application requires. This method should
         * only be called once.
         * @param factory the factory to add
         * @return self
         */
        Builder adapterFactory(TypeAdapterFactory factory);

        /**
         * Set the port for the server to bind to.
         * @param port the port
         * @return self
         */
        Builder port(int port);

        /**
         * Set the host name for the server to bind to
         * @param hostName the host name
         * @return self
         */
        Builder hostName(String hostName);

        /**
         * Show the lighter startup banner. By default, the banner is shown.
         * @param show true to show the banner, false to hide it
         * @return self
         */
        Builder showBanner(boolean show);

        /**
         * Add a response transformer that will run for EVERY response generated by lighter. This method
         * can be called multiple times to add multiple transformers. Transformers <strong>are not</strong>
         * guaranteed to run in any order.
         * @param transformer the transformer to be used on responses
         * @return self
         */
        Builder addResponseTransformer(GlobalRequestTransformer transformer);

        /**
         * Construct a Lighter instance using the configuration from this builder
         * @return a new Lighter instance
         */
        Lighter build();
    }
}
