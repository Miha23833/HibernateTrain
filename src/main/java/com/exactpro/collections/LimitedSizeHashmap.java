package com.exactpro.collections;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class LimitedSizeHashmap<K, V> {

    final int size;
    Queue<K> keys = new LinkedList<>();
    HashMap<K, V> cacheHashMap = new HashMap<>();

    public LimitedSizeHashmap(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Cache size must be at least 1!");
        this.size = size;
    }

    public void put(K key, V value){
        if (!cacheHashMap.containsKey(key)) {
            keys.add(key);
            if (keys.size() > size) {
                cacheHashMap.remove(keys.remove());
            }
        }
        cacheHashMap.put(key, value);
    }

    public V get(K key){
        return cacheHashMap.get(key);
    }

    public Set<K> keySet(){
        return cacheHashMap.keySet();
    }

    public boolean containsKey(K key){
        return cacheHashMap.containsKey(key);
    }

    public void removeAll(){
        cacheHashMap.keySet().removeAll(cacheHashMap.keySet());
    }
}
