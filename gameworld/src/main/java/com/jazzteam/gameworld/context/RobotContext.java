package com.jazzteam.gameworld.context;

import com.jazzteam.gameworld.model.robots.Robot;
import com.jazzteam.gameworld.model.robots.RobotType;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class RobotContext {
    private Map<RobotType, Map<String, Robot>> robotCache = new HashMap<>();
    private Map<String, String> robotMapping = new HashMap<>();

    public RobotContext() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("robotMapping.properties");
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

    public void removeRobotFromCache(Robot robot) {
        if(robotCache.get(robot.getType()) != null) {
            robotCache.get(robot.getType()).remove(robot.getId());
        }
    }

    public void cacheRobot(Robot robot) {
        if(robotCache.get(robot.getType()) == null) {
            robotCache.put(robot.getType(), new HashMap<>());
        }
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
}
