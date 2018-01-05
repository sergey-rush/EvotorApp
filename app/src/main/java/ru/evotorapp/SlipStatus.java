package ru.evotorapp;

/**
 * Created by sergey-rush on 20.12.2017.
 */
public enum SlipStatus {

    None(0),
    New(1),
    Registered(2),
    Approved(3),
    Rejected(4),
    Printed(5);

    private final int value;

    private SlipStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SlipStatus fromInt(int value) {
        for (SlipStatus status : SlipStatus.values()) {
            int statusValue = status.getValue();
            if (statusValue == value) {
                return status;
            }
        }
        return SlipStatus.None;//For values out of enum scope
    }

    public static String getName(SlipStatus slipStatus){

        switch (slipStatus){
            case None:
                return "Новая";
            case Registered:
                return "Зарегистрирована";
            case Approved:
                return "Одобрена";
            case Rejected:
                return "Отказано";
            case Printed:
                return "Проведена";
        }

        return null;
    }
}