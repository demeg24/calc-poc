package com.test.calculator.entitites.exceptions;

/**
 * Created by Gabor on 2016.11.01..
 */
public class RetryNeededException extends RuntimeException {

    public RetryNeededException(String message) {
        super(message);
    }
}
