package com.jazzteam.gameworld.model.robots;

import com.jazzteam.gameworld.model.commands.Command;
import com.jazzteam.gameworld.model.commands.CommandType;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Robot implements Runnable {
    private String id;
    private RobotType type;
    private boolean isBusy;
    private BlockingQueue<Command> commandQueue;
    private boolean isDestroyed = false;

    protected Robot(int idNumber, RobotType type, int queueCapacity) {
        this.id = type.toString() + idNumber;
        this.type = type;
        this.commandQueue = new ArrayBlockingQueue<>(queueCapacity);
    }

    @Override
    public void run() {
        Command command = null;
        try {
            do {
                if(command != null) {
                    executeCommand(command);
                }
            } while((command = commandQueue.take()).getType() != CommandType.SELF_DESTRUCTION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(Command command) {
        switch (command.getType()) {
            case GO_FORWARD:
                System.out.println(this.toString() + " go forward.");
                break;
            case GO_BACKWARD:
                System.out.println(this.toString() + " go backward.");
                break;
            case TURN_LEFT:
                System.out.println(this.toString() + " turn left.");
                break;
            case TURN_RIGHT:
                System.out.println(this.toString() + " turn right.");
                break;
            case SELF_DESTRUCTION:
                destroyRobot();
                System.out.println(this.toString() + " destroyed.");
                break;
            default:
                System.out.println(this.toString() + " doesn't support command " + command.getType() + ".");
        }
    }

    public void setCommand(Command command) {
        commandQueue.add(command);
    }

    public void destroyRobot() {
        this.isDestroyed = true;
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

    public BlockingQueue<Command> getBlockingQueue() {
        return commandQueue;
    }

    public void setBlockingQueue(BlockingQueue<Command> commandQueue) {
        this.commandQueue = commandQueue;
    }

    @Override
    public String toString() {
        return type.toString() +" robot with id='" + id + "'";
    }
}
