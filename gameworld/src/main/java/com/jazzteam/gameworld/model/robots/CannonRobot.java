package com.jazzteam.gameworld.model.robots;

import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.commands.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CannonRobot extends Robot {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public CannonRobot(int idNumber, int queueCapacity) {
        super(idNumber, RobotType.CANNON, queueCapacity);
        executableCommandTypes.add(CommandType.CANNON_FIRE);
    }

    @Override
    public void executeCommand(Command command) {
        switch (command.getType()) {
            case CANNON_FIRE:
                log.info(this.toString() + " produce cannon volley.");
                break;
            default:
                super.executeCommand(command);
        }
    }
}
