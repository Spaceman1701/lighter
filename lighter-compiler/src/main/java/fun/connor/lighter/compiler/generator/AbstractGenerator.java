package fun.connor.lighter.compiler.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import java.io.IOException;

/**
 * Template class for Java class file generators. This class provides a template for generating classes during
 * compile time and writing them to the build output. Implementations generate their type in the
 * {@link AbstractGenerator#generateType()} method and produce the package name of their type in the
 * {@link AbstractGenerator#getGeneratedPackageName()} method.
 */
public abstract class AbstractGenerator {

    public static final String GENERATED_PACKAGE_NAME = "fun.connor.lighter.generated";

    private Filer filer;

    protected AbstractGenerator(Filer filer) {
        this.filer = filer;
    }

    /**
     * Generate the the Java type and write its source to the build
     * @return the generated type information
     * @throws IOException if the filer cannot write to disk
     */
    public GeneratedType generateCodeFile() throws IOException {
        TypeSpec spec = generateType();
        String packageName = getGeneratedPackageName();
        writeFile(packageName, spec);
        return new GeneratedType(packageName, spec.name);
    }

    private void writeFile(String packageName, TypeSpec type) throws IOException {
        JavaFile file = JavaFile.builder(packageName, type)
                .indent("    ")
                .build();
        file.writeTo(filer);
    }

    /**
     * Generate the Java type to write to the disk. Implementations of this method
     * should not have side effects.
     * @return A TypeSpec for the generated type
     */
    protected abstract TypeSpec generateType();

    /**
     * Get the package name of the generated type. This package name will be used when creating the Java source
     * for the generated file
     * @return the package name as a String
     */
    protected abstract String getGeneratedPackageName();
}
