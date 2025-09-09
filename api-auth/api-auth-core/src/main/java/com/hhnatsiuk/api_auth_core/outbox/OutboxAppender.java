package com.hhnatsiuk.api_auth_core.outbox;

public interface OutboxAppender {
    void appendEmailVerified(String accountUuid, String email);
    void appendUserDeleted(String accountUuid);
}
