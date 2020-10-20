package com.exactpro.collections;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * {@code LimitedSizeHashmap} contains keys in queue.
 * When count of keys equals to size then
 * oldest key {@link K} removing in queue and hashmap cascade.
 */
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
            if (keys.size() > size) {
                K lastKey = keys.remove();
                cacheHashMap.remove(lastKey);
            }
            keys.add(key);
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

    public void clear(){
        cacheHashMap.clear();
        keys.clear();
    }

    public void removeKey(K key){
        if (!cacheHashMap.containsKey(key)){
            return;
        }
        cacheHashMap.remove(key);
        keys.remove(key);
    }

    public int size(){
        return cacheHashMap.size();
    }

    public int maxSize(){
        return size;
    }
}
