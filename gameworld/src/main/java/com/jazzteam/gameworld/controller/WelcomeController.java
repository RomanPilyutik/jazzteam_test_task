package com.jazzteam.gameworld.controller;

import com.jazzteam.gameworld.context.RobotContext;
import com.jazzteam.gameworld.model.commands.CommandType;
import com.jazzteam.gameworld.service.RobotTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class WelcomeController {
    @Autowired
    private RobotTrackerService robotTrackerService;

    @Autowired
    private RobotContext robotContext;

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("robots", robotContext.getAllCachedRobots());
        model.put("commands", CommandType.values());
        return "welcome";
    }
}
