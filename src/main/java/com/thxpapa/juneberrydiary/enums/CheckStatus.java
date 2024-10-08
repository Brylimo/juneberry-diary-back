package com.thxpapa.juneberrydiary.enums;

public enum CheckStatus {
    NONE("none"),
    UNFINISHED("unfinished"),
    COMPLETED("completed");

    private final String value;

    CheckStatus(String value) {
        this.value = value;
    }

    // 문자열로부터 enum을 찾는 메서드
    public static CheckStatus fromValue(String value) {
        for (CheckStatus status : CheckStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}