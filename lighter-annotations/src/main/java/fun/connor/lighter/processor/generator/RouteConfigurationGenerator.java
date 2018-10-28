package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.autoconfig.LighterRouter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.util.List;

public class RouteConfigurationGenerator extends AbstractGenerator {
    //fun.connor.lighter.generated.routing.GeneratedRouteConfiguration

    private List<GeneratedEndpoint> endpoints;

    public RouteConfigurationGenerator(Filer filer, List<GeneratedEndpoint> endpoints) {
        super(filer);
        this.endpoints = endpoints;
    }

    @Override
    protected TypeSpec generateType() {
        MethodSpec getRouterMethod = generateGetRouterMethod();

        return TypeSpec.classBuilder("GeneratedRouteConfiguration")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
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
