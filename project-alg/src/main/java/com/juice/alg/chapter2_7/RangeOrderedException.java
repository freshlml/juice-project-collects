package com.juice.alg.chapter2_7;

import lombok.Getter;

public class RangeOrderedException extends RuntimeException {
    private int begin;
    private int end;
    @Getter
    private int q;

    public RangeOrderedException(int begin, int end, int q) {
        super("Range[" + begin + ", " + end + ") itself ordered!!!");
        this.begin = begin;
        this.end = end;
        this.q = q;
    }

}
