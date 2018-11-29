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

/**
 * Java source code generator for {@link ReverseInjector} implementations. This generator dynamically creates the
 * implementation based on the actual dependencies of the application as determined at compile time. It generates source
 * code with {@link javax.inject} and Java Beans compatible setters. This allows the generated reverse injector object
 * to act as a configuration bean for the actual application implementation. For more details, see
 * {@link ReverseInjector}.
 * <p>
 *     Currently, this object generates implementations which use a {@link HashMap} to map between {@link Class} objects
 *     and instances of the classes.
 * </p>
 * <p>
 * Future versions of this class may change the implementation to use {@link javax.inject.Provider} methods
 * to mock the behavior that directly passing the injector object to Lighter would create.
 * </p>
 */
public class ReverseInjectorGenerator extends AbstractGenerator {

    private Set<DeclaredType> dependencies;

    private LighterTypes types;

    /**
     * Create a reverse injector
     * @param dependencies the application dependencies
     * @param types type utils
     * @param filer filer for writing Java source files
     */
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
