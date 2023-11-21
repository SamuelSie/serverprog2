package com.yrgo.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class PerformanceTimingAdvice {
    public Object performanceTimingMeasurement (ProceedingJoinPoint method) throws Throwable {
        long startTime = System.nanoTime();

        try{
            Object value = method.proceed();
            return value;
        } finally {
            long endTime = System.nanoTime();
            long timeTaken = endTime - startTime;
            System.out.println("Time taken for the method " + method.getSignature().getName() + "() from class " + method.getSignature().getDeclaringType().getSimpleName() +  " took " + timeTaken/1000000 + "ms");
        }
    }

    public void beforeAdviceTesting(JoinPoint jp) {
        System.out.println("Now enterinng method: " + jp.getSignature().getName());
    }
}
