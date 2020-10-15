package com.exactpro.collections.tuple;

import java.util.Objects;

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

    // Поскльку нам нужно получить из кэша по значению этого класса, а экземпляр будет создаваться каждый раз по-новой,
    // тогда осмысленно сделать переопределение по hashcode и equals, чтобы одинаковые по значению, но не по ссылке
    // экземпляры указывали на один и тот же объект
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwoParameterTuple<?, ?> that = (TwoParameterTuple<?, ?>) o;
        return Objects.equals(firstValue, that.firstValue) &&
                Objects.equals(secondValue, that.secondValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstValue, secondValue);
    }
}
