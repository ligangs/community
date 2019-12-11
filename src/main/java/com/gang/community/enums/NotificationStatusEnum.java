package com.gang.community.enums;

public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1),
    ;

    private int Status;

    NotificationStatusEnum(int status) {
        Status = status;
    }

    public int getStatus() {
        return Status;
    }
}
