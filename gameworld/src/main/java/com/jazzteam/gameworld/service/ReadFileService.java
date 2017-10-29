package com.jazzteam.gameworld.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class ReadFileService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Map getRemainingLinesInFile(String filePath, Integer printedLines) {
        Map result = new HashMap<>();
        List<String> remainingLines = new ArrayList<>();
        Integer countOfReadLines = printedLines;
        if(countOfReadLines == null) {
            countOfReadLines = 0;
        }
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.skip(countOfReadLines).forEach(remainingLines::add);
            countOfReadLines += remainingLines.size();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        result.put("lines", remainingLines);
        result.put("printedLines", countOfReadLines);
        return result;
    }
}
