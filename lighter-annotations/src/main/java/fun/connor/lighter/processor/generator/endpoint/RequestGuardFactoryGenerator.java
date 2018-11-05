package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.Expression;
import fun.connor.lighter.processor.model.RequestGuardFactory;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class RequestGuardFactoryGenerator {

    private FieldSpec field;
    private LighterTypes types;

    private DeclaredType type;
    private DeclaredType producesType;

    public RequestGuardFactoryGenerator(RequestGuardFactory factory, LighterTypes types) {
        this.type = factory.getType();
        this.producesType = factory.getProduces();

        this.types = types;

        String name = producesType.toString().replace('.', '_') + "requestGuardFactory";

        field = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    
}
