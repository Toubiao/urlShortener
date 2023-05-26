package ch.hos6.openweb.urlShortener.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ch.hos6.openweb.urlShortener.aspect.RecordTime;

/**
 * Aspect class for recording the execution time of methods.
 * <p>
 * This class uses Spring AOP to create an aspect that records the execution time
 * of every method it's applied to. The execution time is logged as an info message.
 * </p>
 *
 *  @author Toubia Oussama
 */
@Aspect
@Component
public class RecordTimeAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Records the execution time of a method.
     * <p>
     * This method is executed before and after the method it's applied to.
     * It records the time before the method is called, and again after it returns,
     * and then logs the difference as the execution time.
     * </p>
     *
     * @param joinPoint The join point of the method execution.
     * @param recordTime The annotation that was used to apply this aspect.
     * @return The result of the method execution.
     * @throws Throwable if an error occurs during the method execution.
     */
    @Around("@annotation(recordTime)")
    public Object recordTime(ProceedingJoinPoint joinPoint, RecordTime recordTime)
            throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            //log time
            log.info("{} taking {} ms", joinPoint.toShortString(),time);
        }
    }
}
