package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.*;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.handler.*;
import fun.connor.lighter.injection.InjectionObjectFactory;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;
import fun.connor.lighter.processor.model.ModelUtils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.*;

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

    private Types typeUtils;

    public EndpointRequestResolverGenerator(Controller controller, Endpoint endpoint, Types typeUtils, Filer filer) {
        super(filer);
        this.controller = controller;
        this.endpoint = endpoint;

        controllerTypeName = TypeName.get(controller.getElement().asType());

        this.typeUtils = typeUtils;
    }


    @Override
    protected TypeSpec generateType() {
        FieldSpec controllerField = generateControllerField();
        List<FieldSpec> adapterFields = generateAdapterFields();
        MethodSpec constructor = generateConstructor();
        MethodSpec resolveMethod = generateResolveMethod();

        return TypeSpec.classBuilder(makeClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(LighterRequestResolver.class)
                .addField(controllerField)
                .addFields(adapterFields)
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

    private List<FieldSpec> generateAdapterFields() {
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        Set<TypeName> requiredAdapters = getAllAdaptableTypes();

        for (TypeName type : requiredAdapters) {
            TypeName adapterType = ParameterizedTypeName.get(ClassName.get(TypeAdapter.class), type);
            String fieldName = getTypeAdapterName(type);
            FieldSpec spec = FieldSpec.builder(adapterType, fieldName, Modifier.PRIVATE).build();
            fieldSpecs.add(spec);
        }

        return fieldSpecs;
    }

    private CodeBlock makeTypeAdaptersAndSetFields(String factoryName)  {
        CodeBlock.Builder block = CodeBlock.builder();
        Set<TypeName> requiredAdapters = getAllAdaptableTypes();

        for (TypeName type : requiredAdapters) {
            String adapterName = getTypeAdapterName(type);
            block.addStatement("$L = $L.getAdapter($T.class)", adapterName, factoryName, type);
        }

        return block.build();
    }

    private String getTypeAdapterName(TypeName type) {
        return type.toString().replace('.', '_') + "Adapter";
    }

    private Set<TypeName> getAllAdaptableTypes() {
        Set<TypeName> types = new HashSet<>();

        List<TypeMirror> argumentTypes = endpoint.getMethodArgumentTypes();
        for (TypeMirror type : argumentTypes) {
            TypeMirror typeToAdd = type;
            if (ModelUtils.typeMirrorEqualsType(type, RequestContext.class)) { //TODO: change to Request when I make that change
                continue;
            }
            if (ModelUtils.typeMirrorEqualsType(type, Optional.class)) {
                DeclaredType declaredType = (DeclaredType) type;
                if (!declaredType.getTypeArguments().isEmpty()) {
                    typeToAdd = declaredType.getTypeArguments().get(0);
                }
            }

            TypeMirror erasedType = typeUtils.erasure(typeToAdd);
            types.add(TypeName.get(erasedType));
        }

        types.add(TypeName.get(typeUtils.erasure(getMethodTypeParameterType())));
        return types;
    }

    private CodeBlock adapteTypeToString(String strVarName, String typedVarName, TypeMirror type) {
        TypeMirror erasedType = typeUtils.erasure(type);
        String typeAdapter = getTypeAdapterName(TypeName.get(erasedType));
        return CodeBlock.builder()
                .addStatement("$L = $L.serialize($L)", strVarName, typeAdapter, typedVarName)
                .build();
    }

    private CodeBlock adapteStringToType(String typedVarName, String strVarName, TypeMirror type) {
        TypeMirror erasedType = typeUtils.erasure(type);
        String typeAdapter = getTypeAdapterName(TypeName.get(erasedType));
        return CodeBlock.builder()
                .addStatement("$L = $L.deserialize($L)", typedVarName, typeAdapter, strVarName)
                .build();
    }

    private CodeBlock generateRequiredParamBlock
            (String parameterNameInMap, String parameterMapName, TypeMirror expectedType, String parameterName) {
        String parameterStrName = parameterName + "_Str";
        return CodeBlock.builder()
                .addStatement("String $L = $L.get($S)", parameterStrName, parameterMapName, parameterNameInMap)
                .beginControlFlow("if ($L == null)", parameterStrName)
                    .addStatement("throw new $T($S, $S, $T.class)", TypeMarshalException.class, "bad", "bad", expectedType)
                .endControlFlow()
                .addStatement("$T $L;", expectedType, parameterName)
                .add(adapteStringToType(parameterName, parameterStrName, expectedType))
                .build();
    }

    private CodeBlock generatedOptionalParamBlock
            (String parameterNameInMap, String parameterMapName, TypeMirror expectedType, String parameterName) {
        String parameterStrName = parameterName + "_Str";
        String paramOptionName = parameterName + "_Optional";
        TypeMirror optionalType = ModelUtils.extractOptionalType((DeclaredType)expectedType);
        TypeName optionalTypeName = TypeName.get(optionalType);
        return CodeBlock.builder()
                .addStatement("String $L = $L.get($S)", parameterStrName, parameterMapName, parameterNameInMap)
                .addStatement("$T $L", optionalTypeName, paramOptionName)
                .beginControlFlow("if ($L == null)", parameterStrName)
                    .addStatement("$L = null", paramOptionName)
                .endControlFlow()
                .beginControlFlow("else")
                    .add(adapteStringToType(paramOptionName, parameterStrName, optionalType))
                .endControlFlow()
                .addStatement("$T<$T> $L = $T.ofNullable($L)", Optional.class, optionalTypeName, parameterName,
                        Optional.class, paramOptionName)
                .build();
    }

    private MethodSpec generateResolveMethod() {

        ParameterizedTypeName mapStrStr = ParameterizedTypeName
                .get(Map.class, String.class, String.class);

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
        return TypeName.get(getMethodTypeParameterType());
    }

    private DeclaredType getMethodTypeParameterType() {
        DeclaredType returnType = (DeclaredType)endpoint.getReturnType();
        return (DeclaredType) returnType.getTypeArguments().get(0);
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
        List<CodeBlock> paramMarshalBlocks = new ArrayList<>();

        for (Endpoint.EndpointParam reqParam : endpoint.getRequiredParams()) {
            paramMarshalBlocks.add(generateRequiredParamBlock
                    (reqParam.getNameInMap(), PATH_PARAMS_NAME, reqParam.getType(), reqParam.getNameOnMethod()));
        }

        for (Endpoint.EndpointParam optParams : endpoint.getOptionalParams()) {

            TypeMirror type = optParams.getType();

            paramMarshalBlocks.add(generatedOptionalParamBlock
                            (optParams.getNameInMap(), PATH_PARAMS_NAME, type, optParams.getNameOnMethod()));
        }

        return paramMarshalBlocks.stream()
                .reduce(CodeBlock.builder().build(), (a, b) -> a.toBuilder().add(b).build());
    }



    private MethodSpec generateConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(InjectionObjectFactory.class, "genericFactory")
                .addParameter(TypeAdapterFactory.class, TYPE_MARSHALLER_NAME)
                .addStatement("this.$L = genericFactory.newInstance($T.class);", CONTROLLER_NAME, controllerTypeName)
                .addCode(makeTypeAdaptersAndSetFields(TYPE_MARSHALLER_NAME))
                .build();
    }


}
