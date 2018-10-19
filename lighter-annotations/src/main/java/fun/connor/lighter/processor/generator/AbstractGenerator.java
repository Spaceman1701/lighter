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

    public abstract void generateCodeFile() throws IOException;

    protected void writeFile(String packageName, TypeSpec type) throws IOException {
        JavaFile file = JavaFile.builder(packageName, type)
                .indent("    ")
                .build();
        file.writeTo(filer);
    }
}
