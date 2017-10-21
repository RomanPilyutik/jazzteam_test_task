package com.jazzteam.gameworld.model.robots;

import com.jazzteam.gameworld.model.commands.Command;

import java.util.concurrent.BlockingQueue;

public class RocketRobot extends Robot {

    public RocketRobot(int idNumber, int queueCapacity) {
        super(idNumber, RobotType.ROCKET, queueCapacity);
    }

    @Override
    public void executeCommand(Command command) {
        switch (command.getType()) {
            case ROCKET_FIRE:
                System.out.println(this.toString() + "produce rocket volley.");
                break;
            default:
                super.executeCommand(command);
        }
    }
}
