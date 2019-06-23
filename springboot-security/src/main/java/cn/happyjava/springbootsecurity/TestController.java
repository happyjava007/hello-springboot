package cn.happyjava.springbootsecurity;

import cn.happyjava.springbootsecurity.config.AdminUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class TestController {

    @Value("${name:happyjava}")
    private String name;

    @GetMapping(value = "/api/needlogin/test1")
    public Object test1(@AuthenticationPrincipal AdminUser adminUser) {
        return adminUser;
    }

    @GetMapping(value = "/api/notneedlogin/test2")
    public Object test2() {
        return name;
    }

    @GetMapping(value = "/api/notneedlogin/login")
    public Object login(HttpSession session) {
        // MOCK 模拟登陆
        session.setAttribute("username", "happyjava");
        return "OK";
    }

}
