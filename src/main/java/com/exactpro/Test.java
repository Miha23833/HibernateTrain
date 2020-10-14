package com.exactpro;


import com.exactpro.entities.Customer;
import com.exactpro.entities.Deal;
import com.exactpro.entities.Product;
import org.apache.log4j.Logger;

import java.util.Date;


class Factory{
    private static Customer customer;
    private static Deal deal;
    private static Product product;
    private static Factory instance;

    public static synchronized Factory getInstance(){
        if(instance == null)
            instance = new Factory();
        return instance;
    }
}


public class Test {

    public static void main(String[] args) {
        Logger infoLogger = Logger.getLogger("releaseeINFO");
        Logger warnLogger = Logger.getLogger("releaseeWARN");

        infoLogger.info("info");
        infoLogger.error("error");
        warnLogger.warn("warn");
        warnLogger.error("error");

    }
}
