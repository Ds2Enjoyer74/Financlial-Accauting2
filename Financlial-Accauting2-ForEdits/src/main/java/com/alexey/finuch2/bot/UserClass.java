package com.alexey.finuch2.bot;

//юзай Lombok
public class UserClass {
    private final String input;
    private boolean isApproved;

    public UserClass(String input) {
        this.input = input;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public String getInput() {
        return input;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

//лишний отступ
}
