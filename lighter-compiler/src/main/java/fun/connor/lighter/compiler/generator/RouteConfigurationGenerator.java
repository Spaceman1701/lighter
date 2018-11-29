package fun.connor.lighter.compiler.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.autoconfig.LighterRouter;
import fun.connor.lighter.autoconfig.RouteConfiguration;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * {@link AbstractGenerator} implementation that generates {@link RouteConfiguration} implementations. This class
 * is used to store the bindings between matchable HTTP methods, route templates, and generated implementations of
 * {@link fun.connor.lighter.handler.LighterRequestResolver}. This generator generates classes with no fields - data
 * is stored in the form of hard-coded behavior in the {@link RouteConfiguration#getRouter()} method. The class itself
 * is decoupled from the interface of {@link fun.connor.lighter.handler.LighterRequestResolver} and probably does not
 * have to change in the event that that interface changes.
 */
public class RouteConfigurationGenerator extends AbstractGenerator {
    //fun.connor.lighter.generated.routing.GeneratedRouteConfiguration

    private List<GeneratedEndpoint> endpoints;

    /**
     * Construct a new RouteConfiguration Generator
     * @param filer the filer used to write generated files
     * @param endpoints the list of all generated endpoint objects
     */
    public RouteConfigurationGenerator(Filer filer, List<GeneratedEndpoint> endpoints) {
        super(filer);
        this.endpoints = endpoints;
    }

    @Override
    protected TypeSpec generateType() {
        MethodSpec getRouterMethod = generateGetRouterMethod();

        return TypeSpec.classBuilder("GeneratedRouteConfiguration")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(RouteConfiguration.class)
                .addMethod(getRouterMethod)
                .build();
    }

    protected String getGeneratedPackageName() {
        return "fun.connor.lighter.generated.routing";
    }

    private MethodSpec generateGetRouterMethod() {
        return MethodSpec.methodBuilder("getRouter")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(LighterRouter.class)
                .addStatement("$T router = new $T()", LighterRouter.class, LighterRouter.class)
                .addCode(makeRoutes())
                .addStatement("return router")
                .build();

    }

    private CodeBlock makeRoutes() {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (GeneratedEndpoint endpoint : endpoints) {
            String method = endpoint.getHttpMethod();
            String template = endpoint.getRouteTemplate();
            builder.addStatement("router.addRoute($S, $S, $T::new)", method, template, endpoint.getHandlerType());
        }
        return builder.build();
    }
}
