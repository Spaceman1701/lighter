package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.handler.ResourceControllerMetaData;
import fun.connor.lighter.processor.model.Controller;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class ControllerDataContainerGenerator extends AbstractGenerator {


    private Controller controller;

    public ControllerDataContainerGenerator(Filer filer, Controller controller) {
        super(filer);
        this.controller = controller;
    }

    public void generateCodeFile() throws IOException {
        TypeSpec type = TypeSpec.classBuilder(controller.getSimpleName() + "MetaData")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ResourceControllerMetaData.class)
                .build();

        String packageName = "fun.connor.lighter.generated." + controller.getContainingName();
        JavaFile file = JavaFile.builder(packageName, type)
                .build();

        writeFile(file);
    }
}
