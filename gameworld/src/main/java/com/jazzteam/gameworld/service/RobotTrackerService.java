package com.jazzteam.gameworld.service;

import com.jazzteam.gameworld.context.RobotContext;
import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.commands.CommandType;
import com.jazzteam.gameworld.model.robots.AlmightyRobot;
import com.jazzteam.gameworld.model.robots.Robot;
import com.jazzteam.gameworld.model.robots.RobotType;
import com.jazzteam.gameworld.model.robots.RocketRobot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RobotTrackerService {

    private ExecutorService executorService;

    @Autowired
    RobotContext robotContext;

    @Value("${thread.pool.capacity}")
    private int threadPoolCapacity;

    @Value("${robot.command.queue.capacity}")
    private int robotCommandQueueCapacity;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(threadPoolCapacity);

        /*RocketRobot rocketRobot = new RocketRobot(1, 10);
        AlmightyRobot almightyRobot = new AlmightyRobot(1, 10);
        List<Robot> robots = new ArrayList<>();
        robots.add(rocketRobot);
        robots.add(almightyRobot);
        Command command1 = new Command(CommandType.BLASTER_FIRE);
        rocketRobot.setCommand(command1);
        almightyRobot.setCommand(command1);
        Command command2 = new Command(CommandType.BLASTER_FIRE);
        rocketRobot.setCommand(command2);
        almightyRobot.setCommand(command2);
        Command command3 = new Command(CommandType.ROCKET_FIRE);
        rocketRobot.setCommand(command3);
        almightyRobot.setCommand(command3);
        Command command4 = new Command(CommandType.CANNON_FIRE);
        rocketRobot.setCommand(command4);
        almightyRobot.setCommand(command4);
        Command command5 = new Command(CommandType.SELF_DESTRUCTION);
        rocketRobot.setCommand(command5);
        almightyRobot.setCommand(command5);
        robots.forEach(executorService::execute);*/
    }

    public void setCommand(Command command, List<String> targetRobotIds) {
        for(String targetRobotId : targetRobotIds) {
            Robot robot = robotContext.getRobotById(targetRobotId);
            if(robot.getBlockingQueue().remainingCapacity() == 0) {
                Robot newRobot = createRobotByType(robot.getType());
                newRobot.setCommand(command);
            } else {
                robot.setCommand(command);
            }
        }
    }

    public void setCommandToAll(Command command) {
        List<Robot> robots = robotContext.getAllCachedRobots();
        for(Robot robot : robots) {
            if (robot.getBlockingQueue().remainingCapacity() == 0) {
                Robot newRobot = createRobotByType(robot.getType());
                newRobot.setCommand(command);
            } else {
                robot.setCommand(command);
            }
        }
    }

    @Scheduled(fixedRate = 1000)
    private void checkCurrentSituationInGameWorld() {
        List<RobotType> possibleRobotTypes = Arrays.asList(RobotType.values());
        List<RobotType> allTypesOfCreatedRobots = robotContext.getCachedRobotTypes();
        possibleRobotTypes.forEach( candidateRobotType -> {
            if (allTypesOfCreatedRobots == null || !allTypesOfCreatedRobots.contains(candidateRobotType)) {
                try {
                    Class<? extends Robot> robotClass = robotContext.getRobotClassByType(candidateRobotType);
                    Robot candidateRobot = robotClass.getConstructor(int.class, int.class).newInstance(1, robotCommandQueueCapacity);
                    robotContext.cacheRobot(candidateRobot);
                    executorService.execute(candidateRobot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Robot createRobotByType(RobotType robotType) {
        Robot robot = null;
        int idNumber = robotContext.getFreeRobotIdNumber(robotType);
        try {
            Class<? extends Robot> robotClass = robotContext.getRobotClassByType(robotType);
            robot = robotClass.getConstructor(int.class, int.class).newInstance(idNumber, robotCommandQueueCapacity);
            robotContext.cacheRobot(robot);
            executorService.execute(robot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return robot;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
