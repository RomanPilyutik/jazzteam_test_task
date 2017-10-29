package com.jazzteam.gameworld.service;

import com.jazzteam.gameworld.context.RobotContext;
import com.jazzteam.gameworld.model.robots.Robot;
import com.jazzteam.gameworld.model.robots.RobotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RobotService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RobotContext robotContext;

    @Value("${robot.command.queue.capacity}")
    private int robotCommandQueueCapacity;

    public Robot createRobot(RobotType robotType) {
        Robot robot = null;
        int idNumber = robotContext.getFreeRobotIdNumber(robotType);
        try {
            log.info("Creating robot with type=" + robotType.toString());
            Class<? extends Robot> robotClass = robotContext.getRobotClassByType(robotType);
            robot = robotClass.getConstructor(int.class, int.class).newInstance(idNumber, robotCommandQueueCapacity);
            log.info(robotType.toString() + " robot with id='" + robot.getId() + "' created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return robot;
    }
}
