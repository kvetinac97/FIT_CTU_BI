package cz.fit.logger;

import cz.fit.logger.service.IService;

public class Logger {

    // Properties
    private final IService service;

    public Logger(IService service) {
        this.service = service;
    }

    public IService getService() {
        return service;
    }

    public void log (String message, Logger.Priority priority ) {
        service.log(message, priority);
    }

    public enum Priority {
        VERBOSE,
        DEBUG,
        NOTICE,
        WARNING,
        ERROR;
    }

}


