package com.exactpro.scheduler.dataExchanger;

import com.exactpro.functional.BooleanFunction;

public class ConditionWaiter {
    private final BooleanFunction<Boolean> function;
    private final int checkTimer;

    public ConditionWaiter(BooleanFunction<Boolean> function, int waitTimer) {
        if (waitTimer < 1){
            throw new IllegalArgumentException("waiting timer can be only positive.");
        }

        this.function = function;
        this.checkTimer = waitTimer;
    }

    public ConditionWaiter(BooleanFunction<Boolean> function){
        this.function = function;
        this.checkTimer = 100;
    }

    public void await() throws InterruptedException {
        while (!function.execute()){
            Thread.sleep(checkTimer);
        }
    }
}
