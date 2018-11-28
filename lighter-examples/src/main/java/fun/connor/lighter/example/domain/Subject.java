package fun.connor.lighter.example.domain;

import fun.connor.lighter.handler.RequestGuard;

/**
 * Interface for an authentication subject {@link RequestGuard}. Requests that require a Subject
 * perform the request on behalf on the subject. Endpoints that require authentication need only
 * to declare a dependency on this interface. See {@link SubjectFactory} for more details.
 */
public interface Subject extends RequestGuard {
    Object getId();
    boolean isAdmin();
}
