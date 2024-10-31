package com.dyma.tennis.service;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String identifier) {
        super("Player with identifier " + identifier + " could not be found.");
    }
}
