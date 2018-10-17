package fun.connor.lighter.processor.step.build;

import fun.connor.lighter.declarative.Get;
import fun.connor.lighter.declarative.Post;
import fun.connor.lighter.declarative.Put;
import fun.connor.lighter.processor.model.Endpoint;
import fun.connor.lighter.processor.model.QueryParams;
import fun.connor.lighter.processor.model.Route;

import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EndpointParser {

    private ExecutableElement element;
    private Set<Class<? extends Annotation>> endpointAnnotations;
    private Route controllerRoute;


    public EndpointParser
            (ExecutableElement element, Set<Class<? extends Annotation>> endpointAnnotations, Route controllerRoute) {
        this.element = element;
        this.endpointAnnotations = endpointAnnotations;
        this.controllerRoute = controllerRoute;
    }

    public List<Endpoint> parse() {

        List<Endpoint> endpoints = new ArrayList<>();
        for (Class<? extends Annotation> annotation : endpointAnnotations) {
            endpoints.add(parseOne(annotation));
        }

        return endpoints;
    }

    private Endpoint parseOne(Class<? extends Annotation> annotationClazz) {
        Annotation annotation = element.getAnnotation(annotationClazz);

        String pathFragment = getEndpointAnnotationValue(annotation);
        Route routeFragment = new Route(pathFragment);
        Endpoint.Method method = getMethodFromAnnotation(annotationClazz);


        fun.connor.lighter.declarative.QueryParams queryParamsAnnotation = element.getAnnotation(fun.connor.lighter.declarative.QueryParams.class);
        QueryParams processor = null;
        if (queryParamsAnnotation != null) {
            processor = new QueryParams(queryParamsAnnotation.value());
        }

        return new Endpoint(method, controllerRoute.append(routeFragment), processor, element);
    }

    private Endpoint.Method getMethodFromAnnotation(Class<? extends Annotation> annotationClazz) {
        if (annotationClazz.equals(Get.class)) { //TODO: create EndpointAnnotation object that wraps all the desperate logic like this
            return Endpoint.Method.GET;
        } else if (annotationClazz.equals(Put.class)) {
            return Endpoint.Method.PUT;
        } else if (annotationClazz.equals(Post.class)) {
            return Endpoint.Method.POST;
        } else {
            return Endpoint.Method.DELETE;
        }
    }

    private String getEndpointAnnotationValue(Annotation endpointAnnotation) {
        try {
            return (String) endpointAnnotation.getClass().getMethod("value").invoke(endpointAnnotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("something went very wrong", e);
        }
    }
}
