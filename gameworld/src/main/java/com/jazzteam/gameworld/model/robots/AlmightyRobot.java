package com.jazzteam.gameworld.model.robots;

import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.commands.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class AlmightyRobot extends Robot {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AlmightyRobot(int idNumber, int queueCapacity) {
        super(idNumber, RobotType.ALMIGHTY, queueCapacity);
        executableCommandTypes.addAll(Arrays.asList(CommandType.ROCKET_FIRE, CommandType.CANNON_FIRE,
                CommandType.BLASTER_FIRE));
    }

    @Override
    public void executeCommand(Command command) {
        switch (command.getType()) {
            case CANNON_FIRE:
                log.info(this.toString() + " produce cannon volley.");
                break;
            case ROCKET_FIRE:
                log.info(this.toString() + " produce rocket volley.");
                break;
            case BLASTER_FIRE:
                log.info(this.toString() + " produce blaster volley.");
                break;
            default:
                super.executeCommand(command);
        }
    }
}
