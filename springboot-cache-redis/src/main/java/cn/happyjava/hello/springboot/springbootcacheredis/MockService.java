package cn.happyjava.hello.springboot.springbootcacheredis;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MockService {

    /**
     * value 缓存的名字，与cacheName是一个东西
     * key 需要缓存的键，如果为空，则会根据参数自动拼接
     * 写法：SpEL 表达式
     */
    @Cacheable(value = "listUsers", key = "#username")
    public List<String> listUsers(String username) {
        System.out.println("执行了listUsers方法");
        return Arrays.asList("Happyjava", "Hello-SpringBoot", System.currentTimeMillis() + "");
    }

}
