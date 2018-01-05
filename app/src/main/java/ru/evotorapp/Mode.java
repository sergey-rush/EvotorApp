package ru.evotorapp;

/**
 * Created by sergey-rush on 11.12.2017.
 */

public enum Mode {
    None(0),
    Slip(1),
    Product(2);

    private final int value;

    private Mode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Mode fromInt(int value) {
        for (Mode status : Mode.values()) {
            int statusValue = status.getValue();
            if (statusValue == value)
            {
                return status;
            }
        }
        return Mode.None;//For values out of enum scope
    }
}
