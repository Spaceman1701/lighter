package fun.connor.lighter.compiler.generator.injection;

import com.squareup.javapoet.*;
import fun.connor.lighter.autoconfig.ReverseInjector;
import fun.connor.lighter.compiler.generator.codegen.SimpleField;
import fun.connor.lighter.injection.InjectionObjectFactory;

import javax.lang.model.element.Modifier;

/**
 * Java code generator for {@link ReverseInjector#getInjector()}. This is a simple method
 * that generates constructs an Anonymous Inner Class that implements the {@link InjectionObjectFactory} interface.
 * This class closes over the {@code classMap} field of the ReverseInjector object. When
 * {@link InjectionObjectFactory#newInstance(Class)} is called it simply looks up an instance in the map.
 */
public class GetInjectorMethodGenerator {

    private SimpleField classMap;

    GetInjectorMethodGenerator(SimpleField classMap) {
        this.classMap = classMap;
    }


    public MethodSpec make() {
       return MethodSpec.methodBuilder("getInjector")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(InjectionObjectFactory.class)
                .addAnnotation(Override.class)
                .addStatement("Map<Class, Object> classMap = $L", classMap.makeReadStub())
                .addStatement("return $L", makeAnonClass())
                .build();

    }

    private TypeSpec makeAnonClass() {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(InjectionObjectFactory.class)
                .addMethod(makeGetInstanceMethod())
                .build();
    }

    private MethodSpec makeGetInstanceMethod() {
        TypeVariableName genericType = TypeVariableName.get("T");
        ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get(Class.class), genericType);
        AnnotationSpec suppressUnchecked = AnnotationSpec.builder(SuppressWarnings.class)
                .addMember("value", "$S", "unchecked")
                .build();
        return MethodSpec.methodBuilder("newInstance")
                .addAnnotation(suppressUnchecked)
                .addTypeVariable(genericType)
                .addModifiers(Modifier.PUBLIC)
                .returns(genericType)
                .addParameter(typeName, "clazz")
                .addStatement("return ($T) classMap.get(clazz)", genericType)
                .build();
    }
}
