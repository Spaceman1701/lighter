package fun.connor.lighter.compiler.generator;

import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.compiler.model.Controller;
import fun.connor.lighter.handler.ResourceControllerMetaData;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

public class ControllerDataContainerGenerator extends AbstractGenerator {


    private Controller controller;

    public ControllerDataContainerGenerator(Filer filer, Controller controller) {
        super(filer);
        this.controller = controller;
    }

    @Override
    public TypeSpec generateType() {
        return TypeSpec.classBuilder(controller.getSimpleName() + "MetaData")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ResourceControllerMetaData.class)
                .build();
    }

    @Override
    protected String getGeneratedPackageName() {
        return "fun.connor.lighter.generated." + controller.getContainingName();
    }
}
