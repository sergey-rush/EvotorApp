package ru.evotorapp;

/**
 * Created by sergey-rush on 24.11.2017.
 */

public enum KeyType {
    NONE(0), NUMBER(1), DELETE(2), SYMBOL(3);

    private final int value;

    private KeyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static KeyType fromInt(int value) {
        for (KeyType keyType : KeyType.values()) {
            int keyValue = keyType.getValue();
            if (keyValue == value)
            {
                return keyType;
            }
        }
        return KeyType.NONE;
    }
}
