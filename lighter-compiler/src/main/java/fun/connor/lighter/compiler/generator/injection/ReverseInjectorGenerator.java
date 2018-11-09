package fun.connor.lighter.compiler.generator.injection;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.autoconfig.ReverseInjector;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.AbstractGenerator;
import fun.connor.lighter.compiler.generator.Utils;
import fun.connor.lighter.compiler.generator.codegen.SimpleField;

import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReverseInjectorGenerator extends AbstractGenerator {

    private Set<DeclaredType> dependencies;

    private LighterTypes types;

    public ReverseInjectorGenerator(Set<DeclaredType> dependencies, LighterTypes types, Filer filer) {
        super(filer);
        this.dependencies = dependencies;
        this.types = types;
    }

    @Override
    protected TypeSpec generateType() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("GeneratedReverseInjector")
                .addSuperinterface(ReverseInjector.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);


        DeclaredType mapClassObj = types.mirrorOfParameterizedClass(Map.class, Class.class, Object.class);
        SimpleField mapField = new SimpleField(mapClassObj, "classMap");
        builder.addField(mapField.getField());

        dependencies.stream()
                .map(t -> makeSetter(mapField, t))
                .forEach(builder::addMethod);

        GetInjectorMethodGenerator injectorMethodGenerator = new GetInjectorMethodGenerator(mapField);
        builder.addMethod(injectorMethodGenerator.make());

        builder.addMethod(makeConstructor());
        return builder.build();
    }

    private MethodSpec makeConstructor() {
        return MethodSpec.constructorBuilder()
                .addAnnotation(Inject.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.classMap = new $T<>()", HashMap.class)
                .build();
    }

    private MethodSpec makeSetter(SimpleField classMap, DeclaredType type) {
        CodeBlock map = classMap.makeReadStub(); //TODO: don't intentionally break this API pls
        return MethodSpec.methodBuilder("set" + Utils.capitalize(type.asElement().getSimpleName().toString()))
                .addAnnotation(Inject.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(TypeName.get(type), "value", Modifier.FINAL)
                .addStatement("$L.put($T.class, value)", map, type)
                .build();
    }

    @Override
    protected String getGeneratedPackageName() {
        return GENERATED_PACKAGE_NAME + ".dependency";
    }
}
