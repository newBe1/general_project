package com.example.uitls;

import cn.hutool.core.util.StrUtil;
import com.example.exception.CustomException;
import com.example.redis.RedisConstant;
import com.example.utils.SerializableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * Description: jddis工具类
 * User: Ryan
 * Date: 2020-06-29
 * Time: 14:16
 */

@Slf4j
@Component
public class JedisUtil {


    private static JedisPool jedisPool;

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        JedisUtil.jedisPool = jedisPool;
    }

    /**
     * 获取Jedis实例
     * @param
     * @return redis.clients.jedis.Jedis
     */
    public static synchronized Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new CustomException("获取Jedis资源异常:" + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 获取redis键值-object
     * @param key
     * @return java.lang.Object
     */
    public static Object getObject(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(key.getBytes());
            if (!StrUtil.isEmptyIfStr(bytes)) {
                return SerializableUtil.unserializable(bytes);
            }
        } catch (Exception e) {
            throw new CustomException("获取Redis键值getObject方法异常:key=" + key + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
        return null;
    }



    /**
     * 设置redis键值-object
     * @param key
     * @param value
     * @return java.lang.String
     */
    public static String setObject(String key, Object value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key.getBytes(), SerializableUtil.serializable(value));
        } catch (Exception e) {
            throw new CustomException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 设置redis键值-object-expiretime
     * @param key
     * @param value
     * @param expiretime
     * @return java.lang.String
     */
    public static String setObject(String key, Object value, int expiretime) {
        String result;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.set(key.getBytes(), SerializableUtil.serializable(value));
            if (RedisConstant.OK.equals(result)) {
                jedis.expire(key.getBytes(), expiretime);
            }
            return result;
        } catch (Exception e) {
            throw new CustomException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 获取redis键值-Json
     * @param key
     * @return java.lang.Object
     */
    public static String getJson(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            throw new CustomException("获取Redis键值getJson方法异常:key=" + key + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 设置redis键值-Json
     * @param key
     * @param value
     * @return java.lang.String
     */
    public static String setJson(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception e) {
            throw new CustomException("设置Redis键值setJson方法异常:key=" + key + " value=" + value + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 设置redis键值-Json-expiretime
     * @param key
     * @param value
     * @param expiretime
     * @return java.lang.String
     */
    public static String setJson(String key, String value, int expiretime) {
        String result;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.set(key, value);
            if (RedisConstant.OK.equals(result)) {
                jedis.expire(key, expiretime);
            }
            return result;
        } catch (Exception e) {
            throw new CustomException("设置Redis键值setJson方法异常:key=" + key + " value=" + value + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 删除key
     * @param key
     * @return java.lang.Long
     */
    public static Long delKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key.getBytes());
        } catch (Exception e) {
            throw new CustomException("删除Redis的键delKey方法异常:key=" + key + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * key是否存在
     * @param key
     * @return java.lang.Boolean
     */
    public static Boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key.getBytes());
        } catch (Exception e) {
            throw new CustomException("查询Redis的键是否存在exists方法异常:key=" + key + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 模糊查询获取key集合(keys的速度非常快，但在一个大的数据库中使用它仍然可能造成性能问题，生产不推荐使用)
     * @param key
     * @return java.util.Set<java.lang.String>
     */
    public static Set<String> keysS(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(key);
        } catch (Exception e) {
            throw new CustomException("模糊查询Redis的键集合keysS方法异常:key=" + key + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 模糊查询获取key集合(keys的速度非常快，但在一个大的数据库中使用它仍然可能造成性能问题，生产不推荐使用)
     * @param key
     * @return java.util.Set<java.lang.String>
     */
    public static Set<byte[]> keysB(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(key.getBytes());
        } catch (Exception e) {
            throw new CustomException("模糊查询Redis的键集合keysB方法异常:key=" + key + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 获取过期剩余时间
     * @param key
     * @return java.lang.String
     */
    public static Long ttl(String key) {
        Long result = -2L;
        try (Jedis jedis = jedisPool.getResource()) {
            result = jedis.ttl(key);
            return result;
        } catch (Exception e) {
            throw new CustomException("获取Redis键过期剩余时间ttl方法异常:key=" + key + " cause by " + e.getMessage());
        }finally {
            closePool();
        }
    }

    /**
     * 释放Jedis资源
     * @param
     * @return void
     */
    public static void closePool() {
        try {
            jedisPool.close();
        } catch (Exception e) {
            throw new CustomException("释放Jedis资源异常:" + e.getMessage());
        }
    }
}
