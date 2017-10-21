package com.jazzteam.gameworld.model.robots;

import com.jazzteam.gameworld.model.commands.Command;

import java.util.concurrent.BlockingQueue;

public class AlmightyRobot extends Robot {

    public AlmightyRobot(int idNumber, int queueCapacity) {
        super(idNumber, RobotType.ALMIGHTY, queueCapacity);
    }

    @Override
    public void executeCommand(Command command) {
        switch (command.getType()) {
            case CANNON_FIRE:
                System.out.println(this.toString() + "produce cannon volley.");
                break;
            case ROCKET_FIRE:
                System.out.println(this.toString() + "produce rocket volley.");
                break;
            case BLASTER_FIRE:
                System.out.println(this.toString() + "produce blaster volley.");
                break;
            default:
                super.executeCommand(command);
        }
    }
}
