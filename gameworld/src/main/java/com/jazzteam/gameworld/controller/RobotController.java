package com.jazzteam.gameworld.controller;

import com.jazzteam.gameworld.context.RobotContext;
import com.jazzteam.gameworld.model.CommandRequest;
import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.commands.CommandType;
import com.jazzteam.gameworld.model.robots.Robot;
import com.jazzteam.gameworld.model.robots.RobotType;
import com.jazzteam.gameworld.service.RobotTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/robot")
public class RobotController {

    @Autowired
    RobotContext robotContext;

    @Autowired
    RobotTrackerService robotTrackerService;

    @RequestMapping(method = RequestMethod.GET, value = "/types")
    public RobotType[] getPossibleRobotTypes() {
        return RobotType.values();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/launchedRobots")
    public List<Robot> getLaunchedRobots() {
        return robotContext.getAllCachedRobots();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/commands")
    public CommandType[] getCommands() {
        return CommandType.values();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/setCommand")
    public String setCommand(@RequestBody CommandRequest commandRequest) {
        robotTrackerService.setCommand(new Command(CommandType.valueOf(commandRequest.commandType.toUpperCase())), Arrays.asList(commandRequest.targetRobotIds));
        return "";
    }
}
