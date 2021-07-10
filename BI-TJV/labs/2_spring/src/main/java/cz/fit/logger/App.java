package cz.fit.logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main ( String[] args ) {
        System.out.println("Maven hey!");

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationConfig.xml");
        Logger logger = context.getBean("logger", Logger.class);
        logger.log("Spring", Logger.Priority.ERROR);

        System.out.println("Maven hey2!");

        ApplicationContext context2 = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Logger logger2 = context2.getBean("logger", Logger.class);
        logger2.log("Spring annotation", Logger.Priority.WARNING);
    }

};
