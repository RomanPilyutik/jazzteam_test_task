package com.jazzteam.gameworld.controller;

import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.commands.CommandType;
import com.jazzteam.gameworld.service.RobotTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class WelcomeController {
    @Autowired
    private RobotTrackerService robotTrackerService;

    @Value("${welcome.message}")
    private String message;

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", message);
        robotTrackerService.setCommandToAll(new Command(CommandType.GO_FORWARD));
        robotTrackerService.setCommandToAll(new Command(CommandType.BLASTER_FIRE));
        robotTrackerService.setCommandToAll(new Command(CommandType.ROCKET_FIRE));
        robotTrackerService.setCommandToAll(new Command(CommandType.SELF_DESTRUCTION));
        return "welcome";
    }
}
