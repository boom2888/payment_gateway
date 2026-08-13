package games;

import com.games.ApiApplication;
import com.games.common.core.redis.RedisCache;
import com.games.payment.domain.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.annotation.Resource;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
public class UpdateCustomInfoToCacheTest {

    @Resource
    private RedisCache redisCache;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private String buildKey(Long userId) {
        return "user_id:" + userId;
    }

    @AfterEach
    public void cleanUp() {
        // 清理测试用的key
        redisCache.deleteObject(buildKey(123L));
        redisCache.deleteObject(buildKey(456L));
        redisCache.deleteObject(buildKey(789L));
    }

    @Test
    public void testUpdateToCache_within24Hours() {
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        if (factory == null) {
            System.out.println("RedisConnectionFactory is null");
            return;
        }

        String host = "unknown";
        int port = -1;
        int db = -1;

        if (factory instanceof LettuceConnectionFactory) {
            LettuceConnectionFactory lettuceFactory = (LettuceConnectionFactory) factory;
            host = lettuceFactory.getHostName();
            port = lettuceFactory.getPort();
            db = lettuceFactory.getDatabase();
        } else if (factory instanceof JedisConnectionFactory) {
            JedisConnectionFactory jedisFactory = (JedisConnectionFactory) factory;
            host = jedisFactory.getHostName();
            port = jedisFactory.getPort();
            db = jedisFactory.getDatabase();
        } else {
            System.out.println("Unsupported RedisConnectionFactory type: " + factory.getClass());
        }

        System.out.println("Redis connected to host: " + host + ", port: " + port + ", db: " + db);
        Customer customer = new Customer();
        customer.setUserId(123L);
        customer.setCreatedAt(new Date());

//        service.updateToCache(customer);

        // 断言缓存中有对应的对象
        Object cached = redisCache.getCacheObject(buildKey(123L));
        assertNotNull(cached);
        assertTrue(cached instanceof Customer);
        Customer cachedCustomer = (Customer) cached;
        assertEquals(123L, cachedCustomer.getUserId());
        assertEquals(customer.getCreatedAt(), cachedCustomer.getCreatedAt());
    }

    @Test
    public void testUpdateToCache_over24Hours() {
        Customer customer = new Customer();
        customer.setUserId(456L);
        Date twoDaysAgo = new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L);
        customer.setCreatedAt(twoDaysAgo);

//        service.updateToCache(customer);

        // 断言缓存中没有该key
        Object cached = redisCache.getCacheObject(buildKey(456L));
        assertNull(cached);
    }

    @Test
    public void testUpdateToCache_userIdNull() {
        Customer customer = new Customer();
        customer.setUserId(null);
        customer.setCreatedAt(new Date());
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateToCache(customer));
//        assertTrue(ex.getMessage().contains("userId is null"));
        // 不会写入缓存
        assertNull(redisCache.getCacheObject(buildKey(null)));
    }

    @Test
    public void testUpdateToCache_createdAtNull() {
        Customer customer = new Customer();
        customer.setUserId(789L);
        customer.setCreatedAt(null);
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateToCache(customer));
//        assertTrue(ex.getMessage().contains("createdAt is null"));
//        assertNull(redisCache.getCacheObject(buildKey(789L)));
    }

    @Test
    public void test1234325() {
        Long l = redisCache.countZSetValuesInRange("1234", 0, 6);
        System.out.println(l);
    }
}
