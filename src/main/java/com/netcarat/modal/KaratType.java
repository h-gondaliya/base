package com.netcarat.modal;

public enum KaratType {
    KT_18("18KT"),
    KT_22("22KT"),
    KT_24("24KT");

    private final String value;

    KaratType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static KaratType fromValue(String value) {
        for (KaratType type : KaratType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown karat type: " + value);
    }
}