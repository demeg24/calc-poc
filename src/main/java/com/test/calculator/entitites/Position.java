package com.test.calculator.entitites;

/**
 * Created by Gabor on 2016.10.31..
 */
public class Position {
    final int positionId;
    final String description;

    public Position(int positionId, String description) {
        this.positionId = positionId;
        this.description = description;
    }

    public int getPositionId() {
        return positionId;
    }

    public String getDescription() {
        return description;
    }
}
