package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.codegen.Assignable;
import fun.connor.lighter.compiler.generator.codegen.Expression;
import fun.connor.lighter.compiler.generator.codegen.Field;
import fun.connor.lighter.compiler.generator.codegen.MapGenerator;
import fun.connor.lighter.compiler.model.RequestGuardFactory;
import fun.connor.lighter.handler.Request;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Java source code generator for generating usages of {@link fun.connor.lighter.handler.RequestGuardFactory}
 * instances. This generator represents request guard factories that are stored as fields.
 */
public class RequestGuardFactoryGenerator implements Field {

    private FieldSpec field;
    private LighterTypes types;

    private DeclaredType type;
    private DeclaredType producesType;

    /**
     * Construct the code generator from the {@link RequestGuardFactory} model class.
     * @param factory the model class representing the factory being used
     * @param types type utilities
     */
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

    /**
     * Get the type of the factory
     * @return the type of the factory
     */
    public TypeMirror getType() {
        return type;
    }

    /**
     * Get the {@link fun.connor.lighter.handler.RequestGuard} type that this factory produces
     * @return the type the factory produces
     */
    public TypeMirror getProducingType() {
        return producesType;
    }

    /**
     * Create an {@link Assignable} element to assign a value to the field this generator controls.
     * @return an {@link Assignable} representation of the field
     */
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

    /**
     * Generate an expression that represents a call to the
     * {@link fun.connor.lighter.handler.RequestGuardFactory#newInstance(Map, Map, Request, TypeAdapterFactory)} method.
     * @param pathMap a generator which represents the path parameters map
     * @param queryMap a generator which represents the query parameters map
     * @param request a generator which represents the request
     * @param typeAdaptorFactory a generator for the top-level TypeAdapterFactory
     * @return the expression representing the method call with the provided parameters
     */
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
