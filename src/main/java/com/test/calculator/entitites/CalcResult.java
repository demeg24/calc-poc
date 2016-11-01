package com.test.calculator.entitites;

/**
 * Created by Gabor on 2016.10.30..
 */
public class CalcResult {

    private final long result;
    private final String status;

    public CalcResult(long result, String status) {
        this.result = result;
        this.status = status;
    }

    public long getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }
}
