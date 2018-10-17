package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.*;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.handler.TypeMarshalException;
import fun.connor.lighter.handler.TypeMarshaller;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class EndpointRequestResolverGenerator extends AbstractGenerator {

    private Controller controller;
    private Endpoint endpoint;

    private TypeName controllerTypeName;

    public EndpointRequestResolverGenerator(Controller controller, Endpoint endpoint, Filer filer) {
        super(filer);
        this.controller = controller;
        this.endpoint = endpoint;

        controllerTypeName = TypeName.get(controller.getElement().asType());
    }


    @Override
    public void generateCodeFile() throws IOException {
        FieldSpec controllerField = generateControllerField();
        FieldSpec unmarshallerField = generateUnmarshallerField();
        MethodSpec constructor = generateConstructor();
        MethodSpec resolveMethod = generateResolveMethod();

        TypeSpec type = TypeSpec.classBuilder(makeClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(LighterRequestResolver.class)
                .addField(controllerField)
                .addField(unmarshallerField)
                .addMethod(constructor)
                .addMethod(resolveMethod)
                .build();

        String packageName = "fun.connor.lighter.generated." + controller.getContainingName();

        writeFile(packageName, type);
    }

    private String makeClassName() {
        String methodName = endpoint.getMethodName();

        String randomStr = UUID.nameUUIDFromBytes((endpoint.getHttpMethod().toString() + endpoint.pathTemplate())
                .getBytes()).toString().replace("-", "");

        return methodName + randomStr;
    }

    private FieldSpec generateControllerField() {
        return FieldSpec.builder(controllerTypeName, "controller", Modifier.PRIVATE)
                .build();
    }

    private FieldSpec generateUnmarshallerField() {
        return FieldSpec.builder(TypeMarshaller.class, "typeMarshaller", Modifier.PRIVATE)
                .build();
    }

    private MethodSpec generateResolveMethod() {

        ParameterizedTypeName mapStrStr = ParameterizedTypeName
                .get(Map.class, String.class, String.class);


        return MethodSpec.methodBuilder("resolve")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapStrStr, "pathParams")
                .addParameter(mapStrStr, "queryParams")
                .addParameter(Request.class, "request")
                .addCode(generateRequiredParamBlock("foo",
                        "pathParams", TypeName.get(Integer.class), "foo"))
                .build();
    }

    private CodeBlock generateResolveMethodCode() {
        return null;
    }

    private CodeBlock generateRequiredParamBlock
            (String parameterMapName, String parameterMap, TypeName expectedType, String parameterName) {



        String parameterStrName = parameterName + "Str";
        return CodeBlock.builder()
                .addStatement("String $L = $L.get($S)", parameterStrName, parameterMap, parameterMapName)
                .beginControlFlow("if ($L == null)", parameterStrName)
                    .addStatement("throw new $T($S, $S, $T.class)", TypeMarshalException.class, "bad", "bad", Void.class)
                .endControlFlow()
                .addStatement("$L $L = typeMarshaller.marshal($L, $T.class)", expectedType,
                        parameterName, parameterStrName, expectedType)
                .build();
    }

    private MethodSpec generateConstructor() {
        return MethodSpec.constructorBuilder()
                .addParameter(controllerTypeName, "controller")
                .addParameter(TypeMarshaller.class, "typeMarshaller")
                .addCode("this.controller = controller;")
                .addCode("this.typeMarshaller = typeMarshaller;")
                .build();
    }


}
