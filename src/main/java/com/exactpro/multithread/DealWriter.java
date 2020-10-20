package com.exactpro.multithread;

import com.exactpro.cache.DealService;
import com.exactpro.entities.Deal;

public class DealWriter extends Thread {

    private final Deal dealToInsert;
    private boolean isDone = false;

    public DealWriter(Deal deal){
        dealToInsert = deal;
    }

    @Override
    public void run() {
        synchronized (this) {
            DealService.insertDeal(dealToInsert);
            isDone = true;
            notifyAll();
        }
    }

    public boolean isDone() {
        return isDone;
    }


}
