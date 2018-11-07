package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.Assignable;
import fun.connor.lighter.processor.generator.codegen.Expression;
import fun.connor.lighter.processor.generator.codegen.Field;
import fun.connor.lighter.processor.generator.codegen.MapGenerator;
import fun.connor.lighter.processor.model.RequestGuardFactory;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestGuardFactoryGenerator implements Field {

    private FieldSpec field;
    private LighterTypes types;

    private DeclaredType type;
    private DeclaredType producesType;

    public RequestGuardFactoryGenerator(RequestGuardFactory factory, LighterTypes types) {
        Objects.requireNonNull(factory, "provided null request guard factory. This is a bug in Lighter.");
        this.type = factory.getType();
        this.producesType = factory.getProduces();

        this.types = types;

        String name = producesType.toString().replace('.', '_') + "requestGuardFactory";

        field = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    @Override
    public FieldSpec getField() {
        return field;
    }

    public TypeMirror getType() {
        return type;
    }

    public TypeMirror getProducingType() {
        return producesType;
    }

    public Assignable getAssignable() {
        return new Assignable() {
            @Override
            public CodeBlock makeAssignmentStub() {
                return CodeBlock.of("this.$N", field);
            }

            @Override
            public TypeMirror getType() {
                return type;
            }
        };
    }

    public Expression makeNewInstance(MapGenerator pathMap, MapGenerator queryMap, RequestGenerator request,
                                      TypeAdaptorFactoryGenerator typeAdaptorFactory) {
        List<Expression> argExpr = new ArrayList<>();
        argExpr.add(pathMap.makeExpression());
        argExpr.add(queryMap.makeExpression());
        argExpr.add(request);
        argExpr.add(typeAdaptorFactory.makeExpression());

        List<CodeBlock> argCode = argExpr.stream()
                .map(Expression::make)
                .collect(Collectors.toList());

        CodeBlock argList = CodeBlock.join(argCode, ", ");
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("this.$N.newInstance($L)", field, argList);
            }

            @Override
            public TypeMirror getType() {
                return producesType;
            }
        };
    }
}
