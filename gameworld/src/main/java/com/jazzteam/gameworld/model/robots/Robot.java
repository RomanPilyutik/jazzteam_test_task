package com.jazzteam.gameworld.model.robots;

import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.commands.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class Robot implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String id;
    private RobotType type;
    private boolean isBusy;
    private BlockingQueue<Command> commandQueue;
    private boolean isDestroyed = false;
    private int idNumber;
    protected List<CommandType> executableCommandTypes = new ArrayList<>();

    protected Robot() {}

    protected Robot(int idNumber, RobotType type, int queueCapacity) {
        this.idNumber = idNumber;
        this.id = type.toString() + idNumber;
        this.type = type;
        this.commandQueue = new ArrayBlockingQueue<>(queueCapacity);
        executableCommandTypes.addAll(Arrays.asList(CommandType.GO_FORWARD, CommandType.GO_BACKWARD,
                CommandType.TURN_LEFT, CommandType.TURN_RIGHT));
    }

    @Override
    public void run() {
        Command command = null;
        try {
            while(true) {
                command = commandQueue.take();
                isBusy = true;
                executeCommand(command);
                isBusy = false;
                if(command.getType() == CommandType.SELF_DESTRUCTION) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(Command command) {
        switch (command.getType()) {
            case GO_FORWARD:
                log.info(this.toString() + " go forward.");
                break;
            case GO_BACKWARD:
                log.info(this.toString() + " go backward.");
                break;
            case TURN_LEFT:
                log.info(this.toString() + " turn left.");
                break;
            case TURN_RIGHT:
                log.info(this.toString() + " turn right.");
                break;
            case SELF_DESTRUCTION:
                this.isDestroyed = true;
                log.info(this.toString() + " destroyed.");
                break;
            default:
                log.info(this.toString() + " doesn't support command " + command.getType() + ".");
        }
    }

    public void setCommand(Command command) {
        commandQueue.add(command);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RobotType getType() {
        return type;
    }

    public void setType(RobotType type) {
        this.type = type;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public BlockingQueue<Command> getCommandQueue() {
        return commandQueue;
    }

    public void setCommandQueue(BlockingQueue<Command> commandQueue) {
        this.commandQueue = commandQueue;
    }

    public void setDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    public List<CommandType> getExecutableCommandTypes() {
        return executableCommandTypes;
    }

    public void setExecutableCommandTypes(List<CommandType> executableCommandTypes) {
        this.executableCommandTypes = executableCommandTypes;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString() {
        return type.toString() +" robot with id='" + id + "'";
    }
}
