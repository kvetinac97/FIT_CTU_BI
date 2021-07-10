package cz.fit.logger.service;

import cz.fit.logger.Logger;

public class SlackService implements IService {

    @Override
    public void log(String message, Logger.Priority priority) {
        System.out.println("[" + priority.name() + "] " + message);
    }

}
