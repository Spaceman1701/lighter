package fun.connor.lighter.injection;

@FunctionalInterface
public interface InjectionObjectFactory {
    <T> T newInstance(Class<T> clazz) throws Exception;
}
