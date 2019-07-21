package cn.happyjava.springbootspringcache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MockService {

    /**
     * value 缓存的名字，与cacheName是一个东西
     * key 需要缓存的键，如果为空，则会根据参数自动拼接
     * 写法：SpEL 表达式
     */
    @Cacheable(value = "listUsers", key = "#username", condition = "#username.equals('Happyjava')")
    public List<String> listUsers(String username) {
        System.out.println("执行了listUsers方法");
        return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
    }

    @CachePut(value = "listUsers", key = "#username", condition = "#username.equals('Happyjava')")
    public List<String> updateCache(String username) {
        System.out.println("执行了updateCache方法");
        return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
    }

    @CacheEvict(value = "listUsers", key = "#username")
    public void deleteCache(String username) {
        System.out.println("执行了deleteCache方法");
    }

    @CacheEvict(value = "listUsers", allEntries = true)
    public void deleteAllCache() {

    }

    /**
     * 符合condition 条件的才会缓存
     */
    @Cacheable(value = "listUsers", condition = "#name.equals('Happyjava')")
    public List<String> getUserByName(String name) {
        return Arrays.asList(name, UUID.randomUUID().toString());
    }

}
