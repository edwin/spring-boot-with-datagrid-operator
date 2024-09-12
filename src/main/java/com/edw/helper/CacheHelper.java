package com.edw.helper;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

/**
 * <pre>
 *  com.edw.helper.CacheHelper
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 12 Sep 2024 19:13
 */
@Service
public class CacheHelper {

    private RemoteCacheManager cacheManager;

    @Autowired
    public CacheHelper (RemoteCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void populate() {
        // build cache without persistence
        RemoteCache cache = cacheManager.getCache("cache-without-persistence");
        if (cache == null) {
            cacheManager.administration().createCache("cache-without-persistence",
            "<distributed-cache name=\"cache-without-persistence\" mode=\"ASYNC\">\n" +
                        "\t<encoding media-type=\"application/x-jboss-marshalling\" />\n" +
                    "</distributed-cache>");
            cache = cacheManager.getCache("cache-without-persistence");
        }

        for (int i = 0; i < 50; i++) {
            cache.put(i, UUID.randomUUID().toString());
        }

        // build cache with persistence
        cache = cacheManager.getCache("cache-with-persistence");
        if (cache == null) {
            cacheManager.administration().createCache("cache-with-persistence",
                    "<distributed-cache name=\"cache-with-persistence\" mode=\"ASYNC\">\n" +
                            "\t<encoding media-type=\"application/x-jboss-marshalling\" />\n" +
                            "\t<persistence passivation=\"false\">\n" +
                            "\t\t<file-store>\n" +
                            "\t\t  <index path=\"/opt/infinispan/server/data\" />\n" +
                            "\t\t  <data path=\"/opt/infinispan/server/data\" />\n" +
                            "\t\t</file-store>\n" +
                            "\t</persistence>\n" +
                            "</distributed-cache>");
            cache = cacheManager.getCache("cache-with-persistence");
        }

        for (int i = 0; i < 50; i++) {
            cache.put(i, UUID.randomUUID().toString());
        }
    }

    public HashMap getCacheWithoutPersistence() {
        RemoteCache<String, String> cache = cacheManager.getCache("cache-without-persistence");

        HashMap hashMap = new HashMap();
        for (String key : cache.keySet()) {
            hashMap.put(key, cache.get(key));
        }
        return hashMap;
    }

    public HashMap getCacheWithPersistence() {
        RemoteCache<String, String> cache = cacheManager.getCache("cache-with-persistence");

        HashMap hashMap = new HashMap();
        for (String key : cache.keySet()) {
            hashMap.put(key, cache.get(key));
        }
        return hashMap;
    }
}
