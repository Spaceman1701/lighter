package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.Filer;
import java.io.IOException;

public abstract class AbstractGenerator {

    private Filer filer;

    protected AbstractGenerator(Filer filer) {
        this.filer = filer;
    }

    public abstract void generateCodeFile() throws IOException;

    public void writeFile(JavaFile file) throws IOException {
        file.writeTo(filer);
    }
}
