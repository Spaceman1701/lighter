package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.handler.ResourceControllerMetaData;
import fun.connor.lighter.processor.model.Controller;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;

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

        writeFile(packageName, type);
    }
}
