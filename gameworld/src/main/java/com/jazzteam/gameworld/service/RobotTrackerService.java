package com.jazzteam.gameworld.service;

import com.jazzteam.gameworld.context.RobotContext;
import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.robots.Robot;
import com.jazzteam.gameworld.model.robots.RobotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RobotTrackerService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ExecutorService executorService;

    @Autowired
    RobotContext robotContext;

    @Autowired
    RobotService robotService;

    @Value("${thread.pool.capacity}")
    private int threadPoolCapacity;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(threadPoolCapacity);
    }

    public void setCommand(Command command, List<String> targetRobotIds) {
        for(String targetRobotId : targetRobotIds) {
            Robot robot = robotContext.getRobotById(targetRobotId);
            log.info("Setting command '" + command.getType().toString() + "' to robot with id='" + targetRobotId + "'.");
            if(robot.getCommandQueue().remainingCapacity() == 0) {
                Robot newRobot = launchRobot(robot.getType());
                newRobot.setCommand(command);
            } else {
                robot.setCommand(command);
            }
        }
    }

    @Scheduled(fixedRate = 500)
    private void checkCurrentSituationInGameWorld() {
        robotContext.getAllCachedRobots().forEach(robot -> {
            if (robot.isDestroyed()) {
                robotContext.removeRobotFromCache(robot);
            }
        });
        List<RobotType> possibleRobotTypes = Arrays.asList(RobotType.values());
        List<RobotType> allTypesOfCreatedRobots = robotContext.getCachedRobotTypes();
        possibleRobotTypes.forEach( candidateRobotType -> {
            if (allTypesOfCreatedRobots == null || !allTypesOfCreatedRobots.contains(candidateRobotType)) {
                try {
                    Robot candidateRobot = robotService.createRobot(candidateRobotType);
                    robotContext.cacheRobot(candidateRobot);
                    executorService.execute(candidateRobot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Robot launchRobot(RobotType robotType) {
        Robot robot = robotService.createRobot(robotType);
        robotContext.cacheRobot(robot);
        executorService.execute(robot);
        return robot;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
