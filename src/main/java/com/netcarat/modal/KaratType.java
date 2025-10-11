package com.netcarat.modal;

import lombok.Getter;

@Getter
public enum KaratType {
    KT_14("14KT"),
    KT_10("10KT"),
    KT_9("9KT"),
    KT_18("18KT"),
    P950("S950"),
    S925("S925");

    private final String value;

    KaratType(String value) {
        this.value = value;
    }

    public static KaratType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (KaratType type : KaratType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        // Return null for unknown values instead of throwing exception
        // This prevents JpaSystemException during AttributeConverter processing
        return null;
    }
}