package cn.happyjava.springbootspringsecurityjwtmybatisplus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author happy
 */
@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

    @GetMapping(value = "/test")
    public Object test() {
        return "OK";
    }

}
