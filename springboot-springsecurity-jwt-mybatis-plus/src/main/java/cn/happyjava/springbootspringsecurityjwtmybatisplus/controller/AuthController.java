package cn.happyjava.springbootspringsecurityjwtmybatisplus.controller;

import cn.happyjava.springbootspringsecurityjwtmybatisplus.security.AdminUser;
import cn.happyjava.springbootspringsecurityjwtmybatisplus.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author happy
 */
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final AdminService adminService;

    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/login")
    public Object login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        return adminService.login(response, username, password);
    }

    @GetMapping(value = "/current")
    public Object currentUser(@AuthenticationPrincipal AdminUser adminUser) {
        return adminUser;
    }

}
