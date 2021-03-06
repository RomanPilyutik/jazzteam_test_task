package com.jazzteam.gameworld.model.robots;

import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.commands.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketRobot extends Robot {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public RocketRobot(int idNumber, int queueCapacity) {
        super(idNumber, RobotType.ROCKET, queueCapacity);
        executableCommandTypes.add(CommandType.ROCKET_FIRE);
    }

    @Override
    public void executeCommand(Command command) {
        switch (command.getType()) {
            case ROCKET_FIRE:
                log.info(this.toString() + " produce rocket volley.");
                break;
            default:
                super.executeCommand(command);
        }
    }
}
