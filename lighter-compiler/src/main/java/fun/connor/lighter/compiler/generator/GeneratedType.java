package fun.connor.lighter.compiler.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * An already generated Java class
 */
public class GeneratedType {

    private String packageName;
    private String className;

    /**
     * Construct a GeneratedType
     * @param packageName the package name of the generated class file
     * @param className the class name of the generated class
     */
    GeneratedType(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }


    /**
     * Get the package name of the generated class
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Get the class name of the generated class
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Get the JavaPoet {@link TypeName} of the generated class
     * @return the type name
     */
    public TypeName getTypeName() {
        return ClassName.get(packageName, className);
    }
}
