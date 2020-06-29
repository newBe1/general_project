package com.example.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * Created with IntelliJ IDEA.
 * Description: 重写Shiro缓存管理器
 * User: Ryan
 * Date: 2020-06-29
 * Time: 15:43
 */
public class CustomCacheManager implements CacheManager
{
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new CustomCache<K,V>();
    }
}
