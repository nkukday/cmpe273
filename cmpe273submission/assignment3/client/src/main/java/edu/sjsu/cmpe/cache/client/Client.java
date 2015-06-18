package edu.sjsu.cmpe.cache.client;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class Client {
    public static void main(String[] args) throws Exception {

        System.out.println("Starting Cache Client...");

        //consistentHash();
        rendezvousHash();

        System.out.println("Exiting Cache Client...");
    }

    public static void consistentHash() {
        ArrayList<CacheServiceInterface> cacheServers = new ArrayList();
        cacheServers.add(new DistributedCacheService("http://localhost:3000"));
        cacheServers.add(new DistributedCacheService("http://localhost:3001"));
        cacheServers.add(new DistributedCacheService("http://localhost:3002"));

        System.out.println("Consistent Hashing starts...");

        int numberOfReplications =  10;
        ConsistentHash consistentHash = new ConsistentHash(Hashing.md5(), numberOfReplications , cacheServers);

        for(long key = 1 ; key <= 10 ; key++)
        {
            String value = Character.toString((char)('a' + key - 1));
            consistentHash.get(key).put(key, value);
            int bucket = cacheServers.indexOf(consistentHash.get(key));
            System.out.println("put(" + key + " => " + value + ") ==> routed to :" + ((DistributedCacheService) cacheServers.get(bucket)).getUrl());
        }

        for(long key = 1; key <= 10 ; key++)
        {
            System.out.println("get(" + key + ") => " + consistentHash.get(key).get(key));
        }
        System.out.println("Consistent Hashing ends...");

    }

    public static void rendezvousHash() {
        System.out.println("Rendezvous Hashing starts...");

        ArrayList<CacheServiceInterface> cacheServers = new ArrayList();
        cacheServers.add(new DistributedCacheService("http://localhost:3000"));
        cacheServers.add(new DistributedCacheService("http://localhost:3001"));
        cacheServers.add(new DistributedCacheService("http://localhost:3002"));

        Funnel<CharSequence> strFunnel = Funnels.stringFunnel(Charset.defaultCharset());
        Funnel<Long> longFunnel = Funnels.longFunnel();

        ArrayList<String> cacheUrls = new ArrayList<String>();
        for(CacheServiceInterface c : cacheServers ) {
            DistributedCacheService d = (DistributedCacheService)c;
            cacheUrls.add(d.getUrl());
        }

        RendezvousHash rendezvousHash = new RendezvousHash(Hashing.md5(),longFunnel,strFunnel,cacheUrls);

        for(long key = 1 ; key <= 10 ; key++)
        {
            String value = Character.toString((char)('a' + key - 1));
            // returns url of cache
            String cacheUrl = (String)rendezvousHash.get(key);
            cacheServers.get(cacheUrls.indexOf(cacheUrl)).put(key,value);
            //int bucket = ;
            System.out.println("put(" + key + " => " + value + ") ==> routed to :" + rendezvousHash.get(key));
        }

        for(long key = 1; key <= 10 ; key++)
        {
            String cacheUrl = (String)rendezvousHash.get(key);
            System.out.println("get(" + key + ") => " + cacheServers.get(cacheUrls.indexOf(cacheUrl)).get(key));
        }

        System.out.println("Rendezvous Hashing ends...");
    }
}