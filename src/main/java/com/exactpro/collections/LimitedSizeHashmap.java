package com.exactpro.collections;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * {@code LimitedSizeHashmap} contains keys in queue.
 * When count of keys equals to size then
 * oldest key {@link K} removing in queue and hashmap cascade.
 */
public class LimitedSizeHashmap<K, V> {

    final int size;
    Queue<K> keys = new LinkedList<>();
    HashMap<K, V> cacheHashMap = new HashMap<>();
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public LimitedSizeHashmap(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Cache size must be at least 1!");
        this.size = size;
    }

    public void put(K key, V value){
        readWriteLock.writeLock().lock();
        try {
            if (!cacheHashMap.containsKey(key)) {
                if (keys.size() > size) {
                    K lastKey = keys.remove();
                    cacheHashMap.remove(lastKey);
                }
                keys.add(key);
            }
            cacheHashMap.put(key, value);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public V get(K key) {
        readWriteLock.readLock().lock();
        try {
            return cacheHashMap.get(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public Set<K> keySet() {
        readWriteLock.readLock().lock();
        try {
            return cacheHashMap.keySet();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public boolean containsKey(K key) {
        readWriteLock.readLock().lock();
        try {
            return cacheHashMap.containsKey(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void clear() {
        readWriteLock.readLock().lock();
        try {
            cacheHashMap.clear();
            keys.clear();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void removeKey(K key) {
        readWriteLock.readLock().lock();
        try {
            if (!cacheHashMap.containsKey(key)) {
                return;
            }
            cacheHashMap.remove(key);
            keys.remove(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public int size() {
        readWriteLock.readLock().lock();
        try {
            return cacheHashMap.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public int maxSize(){
        readWriteLock.readLock().lock();
        try {
            return size;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
