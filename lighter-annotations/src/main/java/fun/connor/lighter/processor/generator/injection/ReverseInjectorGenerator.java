package fun.connor.lighter.processor.generator.injection;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.autoconfig.ReverseInjector;
import fun.connor.lighter.processor.generator.AbstractGenerator;
import fun.connor.lighter.processor.generator.Utils;
import fun.connor.lighter.processor.generator.codegen.SimpleField;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReverseInjectorGenerator extends AbstractGenerator {

    private Set<DeclaredType> dependencies;

    public ReverseInjectorGenerator(Set<DeclaredType> dependencies, Filer filer) {
        super(filer);
        this.dependencies = dependencies;
    }

    @Override
    protected TypeSpec generateType() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("GeneratedReverseInjector")
                .addSuperinterface(ReverseInjector.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        List<SimpleField> fields = dependencies.stream()
                .map(t -> new SimpleField(t, makeFieldName(t)))
                .collect(Collectors.toList());

        fields.forEach(f -> builder.addField(f.getField()));
        fields.forEach(f -> builder.addMethod(makeSetter(f)));

        return builder.build();
    }

    private MethodSpec makeSetter(SimpleField field) {
        return MethodSpec.methodBuilder("set" + Utils.capitalize(field.getName()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(TypeName.get(field.getType()), "value", Modifier.FINAL)
                .addStatement(field.makeAssignmentStub() + " = value")
                .build();
    }

    private String makeFieldName(DeclaredType type) {
        return Utils.decapitalize(type.asElement().getSimpleName().toString());
    }

    @Override
    protected String getGeneratedPackageName() {
        return GENERATED_PACKAGE_NAME;
    }
}
