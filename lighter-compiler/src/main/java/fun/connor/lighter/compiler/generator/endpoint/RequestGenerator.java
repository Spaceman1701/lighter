package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.codegen.Expression;
import fun.connor.lighter.handler.Request;

import javax.lang.model.type.TypeMirror;

/**
 * Java source code generator for generating usages of {@link Request}.
 */
public class RequestGenerator implements Expression {

    private Expression source;
    private LighterTypes types;

    /**
     * Create a RequestGenerator from a source
     * @param source expression which produces a {@link Request} at runtime
     * @param types type utilities
     */
    public RequestGenerator(Expression source, LighterTypes types) {
        this.source = source;
        this.types = types;
    }

    /**
     * Generate an expression representing a call to {@link Request#getBody()}
     * @return the expression
     */
    public Expression makeGetBody() {
        return new Expression() {

            @Override
            public TypeMirror getType() {
                return types.mirrorOfClass(String.class);
            }

            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.getBody()", source.makeReadStub());
            }
        };
    }

    /**
     * Generate an expression representing a call to {@link Request#getContentType()}
     * @return the expression
     */
    public Expression makeGetContentType() {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.getContentType()", source.makeReadStub());
            }

            @Override
            public TypeMirror getType() {
                return types.mirrorOfClass(String.class);
            }
        };
    }

    @Override
    public CodeBlock makeReadStub() {
        return source.makeReadStub();
    }

    @Override
    public TypeMirror getType() {
        return types.mirrorOfClass(Request.class);
    }
}

