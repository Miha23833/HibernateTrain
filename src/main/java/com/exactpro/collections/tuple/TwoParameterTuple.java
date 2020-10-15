package com.exactpro.collections.tuple;

public class TwoParameterTuple<P1, P2> {
    private P1 firstValue;
    private P2 secondValue;

    public TwoParameterTuple(P1 firstValue, P2 secondValue){
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public P1 getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(P1 firstValue) {
        this.firstValue = firstValue;
    }

    public P2 getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(P2 secondValue) {
        this.secondValue = secondValue;
    }
}
