package fun.connor.lighter.example.modules;

import fun.connor.lighter.injection.InjectionObjectFactory;
import fun.connor.lighter.processor.model.Endpoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleDaggerInjector implements InjectionObjectFactory {

    ExampleComponent component;

    Map<Class<?>, Method> methods;

    public ExampleDaggerInjector(ExampleComponent component) {
        this.component = component;

        methods = new HashMap<>();

        for (Method m : component.getClass().getMethods()) {
            methods.put(m.getReturnType(), m);
        }
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T newInstance(Class<T> clazz) {
        try {
            return (T) methods.get(clazz).invoke(component);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
