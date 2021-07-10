package cz.fit.logger.service;

import cz.fit.logger.Logger;

public interface IService {

    void log ( String message, Logger.Priority priority );

}
