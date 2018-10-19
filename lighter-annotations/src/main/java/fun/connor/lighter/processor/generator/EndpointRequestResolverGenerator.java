package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.*;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.handler.TypeMarshaller;
import fun.connor.lighter.processor.generator.endpoint.OptionalParamBlockGenerator;
import fun.connor.lighter.processor.generator.endpoint.ParamBlockGenerator;
import fun.connor.lighter.processor.generator.endpoint.RequiredParamBlockGenerator;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
                .addCode(generateParameterMarshalling())
                .build();
    }

    private CodeBlock generateResolveMethodCode() {
        return null;
    }

    private CodeBlock generateParameterMarshalling() {
        List<ParamBlockGenerator> paramMarshalBlocks = new ArrayList<>();

        for (Endpoint.EndpointParam reqParam : endpoint.getRequiredParams()) {
            paramMarshalBlocks.add( new RequiredParamBlockGenerator
                    (reqParam.getNameInMap(), "pathParams", TypeName.get(reqParam.getType()),
                            reqParam.getNameOnMethod()));
        }

        for (Endpoint.EndpointParam optParams : endpoint.getOptionalParams()) {

            TypeMirror type = optParams.getType();

            paramMarshalBlocks.add( new OptionalParamBlockGenerator
                    (optParams.getNameInMap(), "pathParams", TypeName.get(type),
                            optParams.getNameOnMethod()));
        }

        return paramMarshalBlocks.stream()
                .map(ParamBlockGenerator::build)
                .reduce(CodeBlock.builder().build(), (a, b) -> a.toBuilder().add(b).build());
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
