package com.test.calculator.entitites;

/**
 * Created by Gabor on 2016.10.30..
 */
public class PositionInput {
    int retryCount = 0;
    final Position positionPayload;

    public PositionInput(Position positionPayload) {
        this.positionPayload = positionPayload;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Position getPositionPayload() {
        return positionPayload;
    }
}
