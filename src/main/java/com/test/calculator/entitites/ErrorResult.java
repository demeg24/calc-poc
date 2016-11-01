package com.test.calculator.entitites;

/**
 * Created by Gabor on 2016.10.31..
 */
public class ErrorResult {

    private final int positionId;
    private final String errorMessage;

    public ErrorResult(int positionId, String errorMessage) {
        this.positionId = positionId;
        this.errorMessage = errorMessage;
    }

    public int getPositionId() {
        return positionId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
