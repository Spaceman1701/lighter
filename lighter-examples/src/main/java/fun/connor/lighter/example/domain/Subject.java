package fun.connor.lighter.example.domain;

import fun.connor.lighter.handler.RequestGuard;

public interface Subject extends RequestGuard {
    Object getId();
    boolean isAdmin();
}
