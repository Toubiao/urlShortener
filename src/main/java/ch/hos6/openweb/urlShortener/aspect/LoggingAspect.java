package ch.hos6.openweb.urlShortener.aspect;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect-oriented class for logging execution of methods within components
 * annotated with {@link org.springframework.stereotype.Service},
 * {@link org.springframework.stereotype.Repository}, and
 * {@link org.springframework.stereotype.Controller}.
 * <p>
 * Logs the entry and exit of methods, including the method's fully qualified name
 * and the arguments passed to it.
 *
 * @author Toubia Oussama
 */
@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Pointcut that matches all methods within classes annotated with
     * {@link org.springframework.stereotype.Service}.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods() {
    }

    /**
     * Pointcut that matches all methods within classes annotated with
     * {@link org.springframework.stereotype.Controller}.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
    }

    /**
     * Pointcut that matches all methods within classes annotated with
     * {@link org.springframework.stereotype.Repository}.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryMethods() {

    }

    /**
     * Advice that logs the entry of a method. It is applied before the method's execution.
     *
     * @param joinPoint the join point at method execution
     */
    @Before("serviceMethods() || repositoryMethods() || controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {}::{} with args{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),joinPoint.getArgs());
    }

    /**
     * Advice that logs the exit of a method. It is applied after the method's execution.
     *
     * @param joinPoint the join point at method execution
     */
    @After("serviceMethods() || repositoryMethods() || controllerMethods()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("Exiting method: {}::{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }
}
