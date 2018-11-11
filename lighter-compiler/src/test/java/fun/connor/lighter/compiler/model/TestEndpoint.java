package fun.connor.lighter.compiler.model;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.handler.Response;
import io.github.spaceman1701.footing.annotation.RunFootingTest;
import io.github.spaceman1701.footing.api.FootingCompiler;
import org.junit.Assert;
import org.junit.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestEndpoint {

    @Test
    public void testConstructor() {
        MethodSpec method = MethodSpec.methodBuilder("endpoint")
                .addParameter(String.class, "aString")
                .addParameter(Integer.class, "anInt")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(Response.class, Object.class))
                .addStatement("return null")
                .addAnnotation(RunFootingTest.class)
                .build();

        TypeSpec testType = TypeSpec.classBuilder("aController")
                .addMethod(method)
                .build();

        JavaFileObject fileObject = JavaFile.builder("test", testType).build().toJavaFileObject();
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(fileObject);

        Compilation c = FootingCompiler.compileAndRun(objects, (procEnv, roundEnv) -> {
            if (roundEnv.processingOver()) {
                return;
            }
            Set<? extends Element> elements = roundEnv.getRootElements();
            ExecutableElement theElement = null;
            for (Element e : elements) {
               TypeElement type = (TypeElement) e;
               if (type.getQualifiedName().toString().equals("test.aController")) {
                   for (Element e2 : type.getEnclosedElements()) {
                       if (e2.getSimpleName().toString().equals("endpoint")) {
                           theElement = (ExecutableElement) e2;
                       }
                   }
               }
            }
            Assert.assertNotNull(theElement);
            Route route = new Route("/foo/bar");
            Endpoint endpoint = new Endpoint
                    (Endpoint.Method.GET,
                            route,
                            null,
                            null,
                            null,
                            theElement);
            Assert.assertNotNull(endpoint);
        });

        CompilationSubject.assertThat(c).succeededWithoutWarnings();
    }
}
