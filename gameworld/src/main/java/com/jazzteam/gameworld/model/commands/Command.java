package com.jazzteam.gameworld.model.commands;


public class Command {
    private CommandType type;

    public Command(CommandType type) {
        this.type = type;
    }

    public CommandType getType() {
        return type;
    }
}
