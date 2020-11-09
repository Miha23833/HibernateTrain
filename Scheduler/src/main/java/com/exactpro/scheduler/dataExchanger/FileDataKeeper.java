package com.exactpro.scheduler.dataExchanger;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FileDataKeeper {
    private static final Map<String, Map<String, List<String>>> files = new ConcurrentHashMap<>();

    public static Map<String, Map<String, List<String>>> getAllData(){
        return files;
    }

    public static void add(String filename, Map<String, List<String>> data){
        files.put(filename, data);
    }

    public static Set<String> keySet(){
        return files.keySet();
    }

    public static Map<String, List<String>> get(String key){
        return files.get(key);
    }

}
