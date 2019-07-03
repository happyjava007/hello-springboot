package cn.happyjava.springbootspringsecurityjwtmybatisplus.service;

import cn.happyjava.springbootspringsecurityjwtmybatisplus.security.AdminUser;

import javax.servlet.http.HttpServletResponse;

/**
 * @author happy
 */
public interface AdminService {

    public AdminUser login(HttpServletResponse response, String username, String password);

}
