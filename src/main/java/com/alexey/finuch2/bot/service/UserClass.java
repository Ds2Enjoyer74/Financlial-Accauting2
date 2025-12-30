package com.alexey.finuch2.bot.service;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class UserClass {
    private final String input;
    private boolean isApproved;
}
