package eg.mos.sportify.aop;


import eg.mos.sportify.util.LogUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


/**
 * Aspect for logging execution of service layer methods.
 * This class uses AOP to intercept method calls in the service package
 * and log method execution details such as method names, arguments,
 * return values, and exceptions.
 */
@Configuration
@Aspect
public class LoggingAspectAdvice {

    private LogUtil logger;

    /**
     * Logs the execution of a method before it runs.
     *
     * @param joinPoint provides the details of the intercepted method call,
     * including the method signature and arguments.
     */
    @Before("eg.mos.sportify.aop.PointCutConfiguration.serviceLayer()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger = LogUtil.getInstance(joinPoint.getTarget().getClass());
        logger.info("Executing method: " + joinPoint.getSignature().getName() + " with arguments: " + Arrays.toString(joinPoint.getArgs()));
    }


    /**
     * Logs the method execution after it completes successfully, capturing the return value.
     *
     * @param joinPoint provides details of the intercepted method call.
     * @param result the result returned by the method.
     */
    @AfterReturning(pointcut = "eg.mos.sportify.aop.PointCutConfiguration.serviceLayer()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        logger = LogUtil.getInstance(joinPoint.getTarget().getClass());
        logger.info("Method executed: " + joinPoint.getSignature().getName() + " with result: " + result);
    }

    /**
     * Logs an exception thrown by a method in the service layer.
     *
     * @param joinPoint provides details of the intercepted method call.
     * @param exception the exception thrown by the method.
     */
    @AfterThrowing(pointcut = "eg.mos.sportify.aop.PointCutConfiguration.serviceLayer()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        logger = LogUtil.getInstance(joinPoint.getTarget().getClass());
        logger.error("Exception in method: " + joinPoint.getSignature().getName() + " with message: " + exception.getMessage());
    }
}
