package com.jazzteam.gameworld.controller;

import com.jazzteam.gameworld.service.ReadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/log")
public class LogController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ReadFileService readFileService;

    @RequestMapping(method = RequestMethod.GET, value = "/remainingLines")
    public Map getPossibleRobotTypes(@RequestParam(value = "printedLines") Integer printedLines) {
        return readFileService.getRemainingLinesInFile("logs/application.log", printedLines);
    }
}
