package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.*;
import fun.connor.lighter.handler.*;
import fun.connor.lighter.injection.InjectionObjectFactory;
import fun.connor.lighter.processor.generator.endpoint.OptionalParamBlockGenerator;
import fun.connor.lighter.processor.generator.endpoint.ParamBlockGenerator;
import fun.connor.lighter.processor.generator.endpoint.RequiredParamBlockGenerator;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EndpointRequestResolverGenerator extends AbstractGenerator {

    private static final String QUERY_PARAMS_NAME = "queryParams";
    private static final String PATH_PARAMS_NAME = "pathParams";
    private static final String CONTROLLER_NAME = "controller";
    private static final String TYPE_MARSHALLER_NAME = "typeMarshaller";
    private static final String RESOLVE_METHOD_NAME = "resolve";
    private static final String REQUEST_PARAM_NAME = "request";


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
    protected TypeSpec generateType() {
        FieldSpec controllerField = generateControllerField();
        FieldSpec unmarshallerField = generateUnmarshallerField();
        MethodSpec constructor = generateConstructor();
        MethodSpec resolveMethod = generateResolveMethod();

        return TypeSpec.classBuilder(makeClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(LighterRequestResolver.class)
                .addField(controllerField)
                .addField(unmarshallerField)
                .addMethod(constructor)
                .addMethod(resolveMethod)
                .build();
    }

    @Override
    protected String getGeneratedPackageName() {
        return GENERATED_PACKAGE_NAME + "." + controller.getContainingName();
    }

    private String makeClassName() {
        String methodName = endpoint.getMethodName();

        String randomStr = UUID.nameUUIDFromBytes((endpoint.getHttpMethod().toString() + endpoint.pathTemplate())
                .getBytes()).toString().replace("-", "");

        return methodName + randomStr;
    }

    private FieldSpec generateControllerField() {
        return FieldSpec.builder(controllerTypeName, CONTROLLER_NAME, Modifier.PRIVATE)
                .build();
    }

    private FieldSpec generateUnmarshallerField() {
        return FieldSpec.builder(TypeMarshaller.class, TYPE_MARSHALLER_NAME, Modifier.PRIVATE)
                .build();
    }

    private MethodSpec generateResolveMethod() {

        ParameterizedTypeName mapStrStr = ParameterizedTypeName
                .get(Map.class, String.class, String.class);

        TypeName returnTypeParameter = getMethodTypeParameter();

        return MethodSpec.methodBuilder(RESOLVE_METHOD_NAME)
                .returns(TypeName.get(endpoint.getReturnType()))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapStrStr, PATH_PARAMS_NAME)
                .addParameter(mapStrStr, QUERY_PARAMS_NAME)
                .addParameter(Request.class, REQUEST_PARAM_NAME)
                .addCode(generateParameterMarshalling())
                .addCode(generateResolveMethodCode())
                .build();
    }

    private TypeName getMethodTypeParameter() {
        DeclaredType returnType = (DeclaredType)endpoint.getReturnType();
        DeclaredType typeParameter = (DeclaredType) returnType.getTypeArguments().get(0); //will always be length 1

        return TypeName.get(typeParameter);
    }

    private CodeBlock generateResolveMethodCode() {
        List<String> parameters = endpoint.getMethodArgs();
        String methodName = endpoint.getMethodName();
        return CodeBlock.builder()
                .addStatement("$T<$T> context = new $T<>(request)", RequestContext.class, getMethodTypeParameter(), RequestContext.class)
                .addStatement("$T<$T> response = $L.$L($L)", Response.class,
                        getMethodTypeParameter(), "controller", methodName, String.join(",", parameters))
                .addStatement("return response")
                .build();
    }

    private CodeBlock generateParameterMarshalling() {
        List<ParamBlockGenerator> paramMarshalBlocks = new ArrayList<>();

        for (Endpoint.EndpointParam reqParam : endpoint.getRequiredParams()) {
            paramMarshalBlocks.add( new RequiredParamBlockGenerator
                    (reqParam.getNameInMap(), PATH_PARAMS_NAME, TypeName.get(reqParam.getType()),
                            reqParam.getNameOnMethod()));
        }

        for (Endpoint.EndpointParam optParams : endpoint.getOptionalParams()) {

            TypeMirror type = optParams.getType();

            paramMarshalBlocks.add( new OptionalParamBlockGenerator
                    (optParams.getNameInMap(), PATH_PARAMS_NAME, TypeName.get(type),
                            optParams.getNameOnMethod()));
        }

        return paramMarshalBlocks.stream()
                .map(ParamBlockGenerator::build)
                .reduce(CodeBlock.builder().build(), (a, b) -> a.toBuilder().add(b).build());
    }



    private MethodSpec generateConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(InjectionObjectFactory.class, "genericFactory")
                .addParameter(TypeMarshaller.class, TYPE_MARSHALLER_NAME)
                .addStatement("this.$L = genericFactory.newInstance($T.class);", CONTROLLER_NAME, controllerTypeName)
                .addStatement("this.$L = $L;", TYPE_MARSHALLER_NAME, TYPE_MARSHALLER_NAME)
                .build();
    }


}
