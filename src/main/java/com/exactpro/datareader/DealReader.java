package com.exactpro.datareader;

import com.exactpro.cache.DealService;
import com.exactpro.entities.Deal;

public class DealReader implements Runnable {

    private Deal deal = null;
    private final int dealID;
    private boolean isDone = false;

    @Override
    public void run() {
        deal = DealService.getByID(dealID);
        isDone = true;
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
