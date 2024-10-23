package eg.mos.sportify.aop;


import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class that defines reusable pointcuts for the application.
 * This class is used to centralize pointcut expressions for better maintainability and readability.
 */
@Configuration
public class PointCutConfiguration {

    /**
     * Pointcut that matches all methods in the `eg.mos.sportify.service` package
     * and its sub-packages.
     */
    @Pointcut("execution(* eg.mos.sportify.service..*(..))")
    public void serviceLayer() {}
}
