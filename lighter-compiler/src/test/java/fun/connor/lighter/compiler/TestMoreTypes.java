package fun.connor.lighter.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.github.spaceman1701.footing.annotation.RunFootingTest;
import io.github.spaceman1701.footing.api.FootingCompiler;
import org.junit.Assert;
import org.junit.Test;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

public class TestMoreTypes {
    @Test
    public void testIsTypeMirrorOfClass() {
        TypeSpec simpleClass = TypeSpec.classBuilder("Foo")
                .addAnnotation(RunFootingTest.class)
                .build();

        JavaFileObject object = JavaFile.builder("test", simpleClass).build().toJavaFileObject();
        List<JavaFileObject> javaFileObjects = new ArrayList<>();
        javaFileObjects.add(object);

        FootingCompiler.compileAndRun(javaFileObjects, env -> {
            if (env.processingOver()) {
                return;
            }


            TypeMirror arrayList = env.requireTypeByName("java.util.ArrayList").asType();
            Assert.assertTrue(MoreTypes.isTypeMirrorOfClass(arrayList, ArrayList.class));
            Assert.assertFalse(MoreTypes.isTypeMirrorOfClass(arrayList, List.class));

            TypeMirror intMirror = env.getProcessingEnvironment().getTypeUtils().getPrimitiveType(TypeKind.INT);
            Assert.assertTrue(MoreTypes.isTypeMirrorOfClass(intMirror, int.class));
            Assert.assertFalse(MoreTypes.isTypeMirrorOfClass(intMirror, Integer.class));
        });
    }

    @Test
    public void testIsTypeOptional() {
        TypeSpec simpleClass = TypeSpec.classBuilder("Foo")
                .addAnnotation(RunFootingTest.class)
                .build();

        JavaFileObject object = JavaFile.builder("test", simpleClass).build().toJavaFileObject();
        List<JavaFileObject> javaFileObjects = new ArrayList<>();
        javaFileObjects.add(object);

        FootingCompiler.compileAndRun(javaFileObjects, env -> {
            if (env.processingOver()) {
                return;
            }

            TypeMirror objectMirror = env.requireTypeByName("java.lang.Object").asType();
            TypeMirror optionalMirror = env.requireTypeByName("java.util.Optional").asType();
            TypeMirror optionalIntMirror = env.requireTypeByName("java.util.OptionalInt").asType();
            TypeMirror optionalLongMirror = env.requireTypeByName("java.util.OptionalLong").asType();
            TypeMirror optionalDoubleMirror = env.requireTypeByName("java.util.OptionalDouble").asType();

            Assert.assertTrue(MoreTypes.isTypeOptional(optionalMirror));
            Assert.assertTrue(MoreTypes.isTypeOptional(optionalIntMirror));
            Assert.assertTrue(MoreTypes.isTypeOptional(optionalLongMirror));
            Assert.assertTrue(MoreTypes.isTypeOptional(optionalDoubleMirror));

            Assert.assertFalse(MoreTypes.isTypeOptional(objectMirror));
        });
    }
}
