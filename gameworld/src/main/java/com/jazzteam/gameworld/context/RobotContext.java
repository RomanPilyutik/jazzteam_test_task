package com.jazzteam.gameworld.context;

import com.jazzteam.gameworld.model.robots.Robot;
import com.jazzteam.gameworld.model.robots.RobotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class RobotContext {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<RobotType, Map<String, Robot>> robotCache = new HashMap<>();
    private Map<String, String> robotMapping = new HashMap<>();
    private Map<RobotType, Integer> lastUsedRobotIdNumber = new HashMap<>();

    public RobotContext() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = getClass().getClassLoader().getResourceAsStream("robotMapping.properties");
            prop.load(input);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                robotMapping.put(key, value);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Robot getRobotById(String robotId) {
        Robot robot = null;
        for(Map<String, Robot> robotsById : robotCache.values()) {
            robot = robotsById.get(robotId);
            if(robot != null) {
                break;
            }
        }
        if(robot == null) {
            log.info("Robot with id='" + robotId + "' doesn't exist in game world.");
        }
        return robot;
    }

    public void removeRobotFromCache(Robot robot) {
        if(robotCache.get(robot.getType()) != null) {
            robotCache.get(robot.getType()).remove(robot.getId());
        }
        if(robotCache.get(robot.getType()).isEmpty()) {
            robotCache.remove(robot.getType());
        }
    }

    public void cacheRobot(Robot robot) {
        if(robotCache.get(robot.getType()) == null) {
            robotCache.put(robot.getType(), new HashMap<>());
        }
        lastUsedRobotIdNumber.put(robot.getType(), robot.getIdNumber());
        robotCache.get(robot.getType()).put(robot.getId(), robot);
    }

    public boolean isRobotCached(Robot robot) {
        return robotCache.get(robot.getType()) != null && robotCache.get(robot.getType()).get(robot.getId()) != null;
    }

    public List<RobotType> getCachedRobotTypes() {
        return new ArrayList<>(robotCache.keySet());
    }

    public Class getRobotClassByType(RobotType robotType) throws Exception {
        String robotClassName = robotMapping.get(robotType.toString());
        if(robotClassName == null) {
            throw new Exception("Robot type '" + robotType + "' isn't registered in robotMapping.properties file.");
        }
        Class resultClass = Class.forName(robotClassName);
        if(!Robot.class.isAssignableFrom(resultClass)) {
            throw new Exception("Registered class '" + resultClass.toString() + "' isn't a Robot inheritor.");
        }
        return resultClass;
    }

    public int getFreeRobotIdNumber(RobotType robotType) {
        if(lastUsedRobotIdNumber.get(robotType) == null) {
            return 1;
        }
        return lastUsedRobotIdNumber.get(robotType) + 1;
    }

    public List<Robot> getAllCachedRobots() {
        List<Robot> allCachedRobots = new ArrayList<>();
        for(Map<String, Robot> robotsById : robotCache.values()) {
            allCachedRobots.addAll(robotsById.values());
        }
        return allCachedRobots;
    }
}
