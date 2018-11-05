package fun.connor.lighter.handler;

import fun.connor.lighter.adapter.TypeAdapterFactory;

import java.util.Map;

public interface RequestGuardFactory<T extends RequestGuard> {
    T newInstance(Map<String, String> pathParams, Map<String, String> queryParams, Request request,
                  TypeAdapterFactory factory);
}
