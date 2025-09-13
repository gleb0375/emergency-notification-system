package com.hhnatsiuk.api_auth_core.outbox;

public interface OutboxAppender {
    void appendUserCreated(String accountUuid, String email);
    void appendUserDeleted(String accountUuid);
}
