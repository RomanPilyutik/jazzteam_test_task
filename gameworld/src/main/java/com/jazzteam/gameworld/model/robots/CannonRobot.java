package com.jazzteam.gameworld.model.robots;

import com.jazzteam.gameworld.model.commands.Command;

import java.util.concurrent.BlockingQueue;

public class CannonRobot extends Robot {

    public CannonRobot(int idNumber, int queueCapacity) {
        super(idNumber, RobotType.CANNON, queueCapacity);
    }

    @Override
    public void executeCommand(Command command) {
        switch (command.getType()) {
            case CANNON_FIRE:
                System.out.println(this.toString() + "produce cannon volley.");
                break;
            default:
                super.executeCommand(command);
        }
    }
}
