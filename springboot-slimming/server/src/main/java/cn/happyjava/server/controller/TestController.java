package cn.happyjava.server.controller;

import cn.happyjava.core.utils.TestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(value = "/test")
    public Object test() {
        return TestUtils.getUUID();
    }

}
