package com.alexey.finuch2.bot;

public enum UserText {
    START("/start"),
    HELP("/help");

    private final String value;

    UserText(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
