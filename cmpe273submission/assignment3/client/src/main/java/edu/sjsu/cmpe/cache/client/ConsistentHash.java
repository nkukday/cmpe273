package edu.sjsu.cmpe.cache.client;

import java.util.SortedMap;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.hash.*;


public class ConsistentHash{

    private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<Integer, CacheServiceInterface> circle =
            new TreeMap<Integer, CacheServiceInterface>();

    public ConsistentHash(HashFunction hashFunction,int numberOfReplicas, Collection<CacheServiceInterface> nodes) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;
        for (CacheServiceInterface node : nodes) {
            add(node);
        }
    }

    //method hash was not found
    public void add(CacheServiceInterface node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFunction.hashString(node.toString() + i).asInt(),
                    node);
        }
    }

    public void remove(CacheServiceInterface node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hashString(node.toString() + i).asInt());
        }
    }

    public CacheServiceInterface get(long key) {

        if (circle.isEmpty()) {
            return null;
        }

        //hash function not found.since key is long added hashLong
        int hash = hashFunction.hashLong(key).asInt();
        if (!circle.containsKey(hash)) {
            SortedMap<Integer, CacheServiceInterface> tailMap =
                    circle.tailMap(hash);
            hash = tailMap.isEmpty() ?circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

}
