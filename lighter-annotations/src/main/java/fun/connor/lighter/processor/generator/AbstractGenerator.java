package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import java.io.IOException;

public abstract class AbstractGenerator {

    public static final String GENERATED_PACKAGE_NAME = "fun.connor.lighter.generated";

    private Filer filer;

    protected AbstractGenerator(Filer filer) {
        this.filer = filer;
    }

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

    protected abstract TypeSpec generateType();
    protected abstract String getGeneratedPackageName();
}
