package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

public class GeneratedType {

    private String packageName;
    private String className;

    GeneratedType(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }


    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public TypeName getTypeName() {
        return ClassName.get(packageName, className);
    }
}
