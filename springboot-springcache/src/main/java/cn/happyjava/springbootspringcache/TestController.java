package cn.happyjava.springbootspringcache;

import cn.happyjava.springbootspringcache.config.CacheConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final MockService mockService;

    public TestController(MockService mockService) {
        this.mockService = mockService;
    }

    @GetMapping(value = "/listUsers")
    public Object listUsers(String username) {
        return mockService.listUsers(username);
    }

    @GetMapping(value = "/updateCache")
    public Object updateCache(String username) {
        return mockService.updateCache(username);
    }

    @GetMapping(value = "/deleteCache")
    public Object deleteCache(String username) {
        mockService.deleteCache(username);
        return "OK";
    }

    @GetMapping(value = "/deleteAllCache")
    public Object deleteAllCache() {
        mockService.deleteAllCache();
        return "OK";
    }

    @GetMapping(value = "/getUserByName")
    public Object getUserByName(String name) {
        return mockService.getUserByName(name);
    }

}
