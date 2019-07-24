package cn.happyjava.hello.springboot.springbootcacheredis;

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

}
