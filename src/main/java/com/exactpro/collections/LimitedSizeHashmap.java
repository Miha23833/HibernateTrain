package com.exactpro.collections;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class LimitedSizeHashmap<K, V> {

    final int size;
    Queue<K> keys = new LinkedList<>();
    HashMap<K, V> cacheHashMap = new HashMap<>();

    public LimitedSizeHashmap(int size) {this.size = size;}

    public void add(K key, V value){
        if (!cacheHashMap.containsKey(key)) {
            keys.add(key);
            if (keys.size() > size) {
                cacheHashMap.remove(keys.remove());
            }
        }
        cacheHashMap.put(key, value);
    }

    public HashMap<K, V> getCacheHashMap(){
        return cacheHashMap;
    }



}
