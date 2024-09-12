package com.edw.helper;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;
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
        RemoteCache cacheWithoutPersistence = cacheManager.administration().getOrCreateCache("cache-without-persistence",
                new XMLStringConfiguration("<distributed-cache name=\"cache-without-persistence\" mode=\"ASYNC\">\n" +
                        "\t<encoding media-type=\"application/x-jboss-marshalling\" />\n" +
                        "</distributed-cache>")
        );
        for (int i = 0; i < 50; i++) {
            cacheWithoutPersistence.put("key"+i, UUID.randomUUID().toString());
        }

        // build cache with persistence
        RemoteCache cacheWithPersistence = cacheManager.administration().getOrCreateCache("cache-with-persistence",
                new XMLStringConfiguration("<distributed-cache name=\"cache-with-persistence\" mode=\"ASYNC\">\n" +
                        "\t<encoding media-type=\"application/x-jboss-marshalling\" />\n" +
                        "\t<persistence passivation=\"false\">\n" +
                        "\t\t<file-store>\n" +
                        "\t\t  <index path=\"/opt/infinispan/server/data\" />\n" +
                        "\t\t  <data path=\"/opt/infinispan/server/data\" />\n" +
                        "\t\t</file-store>\n" +
                        "\t</persistence>\n" +
                        "</distributed-cache>")
        );
        for (int i = 0; i < 50; i++) {
            cacheWithPersistence.put("key"+i, UUID.randomUUID().toString());
        }
    }

    public HashMap getCacheWithoutPersistence() {
        RemoteCache<String, String> cache = cacheManager.getCache("cache-without-persistence");

        HashMap hashMap = new HashMap();
        for (Object key : cache.keySet()) {
            hashMap.put(key, cache.get(key));
        }
        return hashMap;
    }

    public HashMap getCacheWithPersistence() {
        RemoteCache<String, String> cache = cacheManager.getCache("cache-with-persistence");

        HashMap hashMap = new HashMap();
        for (Object key : cache.keySet()) {
            hashMap.put(key, cache.get(key));
        }
        return hashMap;
    }
}
