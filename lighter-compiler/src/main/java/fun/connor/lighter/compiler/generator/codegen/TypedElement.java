package fun.connor.lighter.compiler.generator.codegen;

import javax.lang.model.type.TypeMirror;

/**
 * A Java source code element that has a type. Typed elements have a
 * fixed type at runtime. All {@link Expression}s and {@link Assignable}s
 * are {@code TypedElement}s.
 */
public interface TypedElement {
    /**
     * Get the type of this element.
     * @return the type a as {@link TypeMirror}
     */
    TypeMirror getType();
}
