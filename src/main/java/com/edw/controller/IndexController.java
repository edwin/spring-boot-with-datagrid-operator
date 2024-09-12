package com.edw.controller;

import com.edw.helper.CacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <pre>
 *  com.edw.controller.IndexController
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 12 Sep 2024 19:11
 */
@RestController
public class IndexController {

    private CacheHelper cacheHelper;

    @Autowired
    public IndexController (CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    @GetMapping(path = "/cache-without-persistence")
    public HashMap cacheWithoutPersistence() {
        return cacheHelper.getCacheWithoutPersistence();
    }

    @GetMapping(path = "/cache-with-persistence")
    public HashMap cacheWithPersistence() {
        return cacheHelper.getCacheWithPersistence();
    }

    @GetMapping(path = "/populate")
    public HashMap populate() {
        cacheHelper.populate();
        return new HashMap() {{
            put("status", "success");
        }};
    }
}
