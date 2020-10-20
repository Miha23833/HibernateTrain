package com.exactpro.multithread;

import com.exactpro.cache.DealService;
import com.exactpro.entities.Deal;

public class DealReader extends Thread {

    private Deal deal = null;
    private final int dealID;
    private boolean isDone = false;

    @Override
    public void run() {
        synchronized (this) {
            deal = DealService.getByID(dealID);
            isDone = true;
            notifyAll();
        }
    }

    public DealReader(int dealID){
        this.dealID = dealID;
    }

    public Deal getDeal() {
        return deal;
    }

    public boolean isDone() {
        return isDone;
    }
}
