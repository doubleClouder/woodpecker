package com.letv.woodpecker.tools;

import java.util.List;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.SafeEncoder;

/**
 * Created by zhusheng on 17/3/13.
 */
public class JedisClient {

    /**
     * 缓存生存时间
     */
    private final int expire = 60000;
    /**
     * 对存储结构为List类型的操作
     */
    public Lists LISTS;
    private static JedisPool jedisPool = null;
    private int maxIdle;
    private String ip;
    private int port;

    private JedisClient() {
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxIdle(10);
//        config.setTestOnBorrow(false);
//        config.setTestOnReturn(false);
//        //redis如果设置了密码：
////        jedisPool = new JedisPool(config, ip,
////                prot,
////                10000,JRedisPoolConfig.REDIS_PASSWORD);
//        //redis未设置了密码：
//        jedisPool = new JedisPool(config,ip,port);
        LISTS = new Lists();
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public JedisPool getPool() {
        if (jedisPool == null) {
            synchronized (this) {
                if (jedisPool == null) {
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxIdle(getMaxIdle());
                    jedisPool = new JedisPool(config, getIp(), getPort());
                }
            }
        }
        return jedisPool;
    }

    /**
     * 从jedis连接池中获取获取jedis对象
     *
     * @return
     */
    public Jedis getJedis() {
        return getPool().getResource();
    }


    /**
     * 回收jedis
     *
     * @param jedis
     */
    public void returnJedis(Jedis jedis) {
        getPool().returnResource(jedis);
    }


    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds
     * @author ruan 2013-4-11
     */
    public void expire(String key, int seconds) {
        if (seconds <= 0) {
            return;
        }
        Jedis jedis = getJedis();
        jedis.expire(key, seconds);
        returnJedis(jedis);
    }

    /**
     * 设置默认过期时间
     *
     * @param key
     * @author ruan 2013-4-11
     */
    public void expire(String key) {
        expire(key, expire);
    }


    public class Lists {
        /**
         * List长度
         *
         * @param key
         * @return 长度
         */
        public long llen(String key) {
            return llen(SafeEncoder.encode(key));
        }

        /**
         * List长度
         *
         * @param key
         * @return 长度
         */
        public long llen(byte[] key) {
            Jedis sjedis = getJedis();
            long count = sjedis.llen(key);
            returnJedis(sjedis);
            return count;
        }

        /**
         * 覆盖操作,将覆盖List中指定位置的值
         *
         * @param key
         * @param index 位置
         * @param value 值
         * @return 状态码
         */
        public String lset(byte[] key, int index, byte[] value) {
            Jedis jedis = getJedis();
            String status = jedis.lset(key, index, value);
            returnJedis(jedis);
            return status;
        }

        /**
         * 覆盖操作,将覆盖List中指定位置的值
         *
         * @param key
         * @param index 位置
         * @param value 值
         * @return 状态码
         */
        public String lset(String key, int index, String value) {
            return lset(SafeEncoder.encode(key), index,
                    SafeEncoder.encode(value));
        }

        /**
         * 在value的相对位置插入记录
         *
         * @param key
         * @param where 前面插入或后面插入
         * @param pivot 相对位置的内容
         * @param value 插入的内容
         * @return 记录总数
         */
        public long linsert(String key, BinaryClient.LIST_POSITION where, String pivot,
                            String value) {
            return linsert(SafeEncoder.encode(key), where,
                    SafeEncoder.encode(pivot), SafeEncoder.encode(value));
        }

        /**
         * 在指定位置插入记录
         *
         * @param key
         * @param where
         * @param pivot 相对位置的内容
         * @param value 插入的内容
         * @return 记录总数
         */
        public long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot,
                            byte[] value) {
            Jedis jedis = getJedis();
            long count = jedis.linsert(key, where, pivot, value);
            returnJedis(jedis);
            return count;
        }

        /**
         * 获取List中指定位置的值
         *
         * @param key
         * @param index 位置
         * @return 值
         **/
        public String lindex(String key, int index) {
            return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
        }

        /**
         * 获取List中指定位置的值
         *
         * @param key
         * @param index 位置
         * @return 值
         **/
        public byte[] lindex(byte[] key, int index) {
            //ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = getJedis();
            byte[] value = sjedis.lindex(key, index);
            returnJedis(sjedis);
            return value;
        }

        /**
         * 将List中的第一条记录移出List
         *
         * @param key
         * @return 移出的记录
         */
        public String lpop(String key) {
            return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
        }

        /**
         * 将List中的第一条记录移出List
         *
         * @param key
         * @return 移出的记录
         */
        public byte[] lpop(byte[] key) {
            Jedis jedis = getJedis();
            byte[] value = jedis.lpop(key);
            returnJedis(jedis);
            return value;
        }

        /**
         * 将List中最后第一条记录移出List
         *
         * @param key
         * @return 移出的记录
         */
        public String rpop(String key) {
            Jedis jedis = getJedis();
            String value = jedis.rpop(key);
            returnJedis(jedis);
            return value;
        }

        /**
         * 向List尾部追加记录
         *
         * @param key
         * @param value
         * @return 记录总数
         */
        public long lpush(String key, String value) {
            return lpush(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 向List头部追加记录
         *
         * @param key
         * @param value
         * @return 记录总数
         */
        public long rpush(String key, String value) {
            Jedis jedis = getJedis();
            long count = jedis.rpush(key, value);
            returnJedis(jedis);
            return count;
        }

        /**
         * 向List头部追加记录
         *
         * @param key
         * @param value
         * @return 记录总数
         */
        public long rpush(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            long count = jedis.rpush(key, value);
            returnJedis(jedis);
            return count;
        }

        /**
         * 向List中追加记录
         *
         * @param key
         * @param value
         * @return 记录总数
         */
        public long lpush(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            long count = jedis.lpush(key, value);
            returnJedis(jedis);
            return count;
        }

        /**
         * 获取指定范围的记录，可以做为分页使用
         *
         * @param key
         * @param start
         * @param end
         * @return List
         */
        public List<String> lrange(String key, long start, long end) {
            //ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = getJedis();
            List<String> list = sjedis.lrange(key, start, end);
            returnJedis(sjedis);
            return list;
        }

        /**
         * 获取指定范围的记录，可以做为分页使用
         *
         * @param key
         * @param start
         * @param end   如果为负数，则尾部开始计算
         * @return List
         */
        public List<byte[]> lrange(byte[] key, int start, int end) {
            //ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = getJedis();
            List<byte[]> list = sjedis.lrange(key, start, end);
            returnJedis(sjedis);
            return list;
        }

        /**
         * 删除List中c条记录，被删除的记录值为value
         *
         * @param key
         * @param c     要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
         * @param value 要匹配的值
         * @return 删除后的List中的记录数
         */
        public long lrem(byte[] key, int c, byte[] value) {
            Jedis jedis = getJedis();
            long count = jedis.lrem(key, c, value);
            returnJedis(jedis);
            return count;
        }

        /**
         * 删除List中c条记录，被删除的记录值为value
         *
         * @param key
         * @param c     要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
         * @param value 要匹配的值
         * @return 删除后的List中的记录数
         */
        public long lrem(String key, int c, String value) {
            return lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
        }

        /**
         * 算是删除吧，只保留start与end之间的记录
         *
         * @param key
         * @param start 记录的开始位置(0表示第一条记录)
         * @param end   记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
         * @return 执行状态码
         */
        public String ltrim(byte[] key, int start, int end) {
            Jedis jedis = getJedis();
            String str = jedis.ltrim(key, start, end);
            returnJedis(jedis);
            return str;
        }

//        /**
//         * 算是删除吧，只保留start与end之间的记录
//         * @param String key
//         * @param int start 记录的开始位置(0表示第一条记录)
//         * @param int end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
//         * @return 执行状态码
//         * */
//        public String ltrim(String key, int start, int end) {
//            return ltrim(SafeEncoder.encode(key), start, end);
//        }
//    }
//
//    public static void main(String[] args) {
//        JedisUtil jedisUtil= JedisUtil.getInstance();
//        JedisUtil.Strings strings=jedisUtil.new Strings();
//        strings.set("nnn", "nnnn");
//        System.out.println("-----"+strings.get("nnn"));
//
//        Jedis jedis=JedisUtil.getInstance().getJedis();
//        for (int i = 0; i < 10; i++) {
//            jedis.set("test", "test");
//            System.out.println(i+"=="+jedis.get("test"));
//
//        }
//        JedisUtil.getInstance().returnJedis(jedis);
//    }
    }
}
