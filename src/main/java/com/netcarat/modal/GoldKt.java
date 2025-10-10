package com.netcarat.modal;

public enum GoldKt {
    KT_18("18KT"),
    S925("S925"),
    KT_14("14KT"),
    KT_10("10KT"),
    KT_9("9KT"),
    P950("P950");

    private final String value;

    GoldKt(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}