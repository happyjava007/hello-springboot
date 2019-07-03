package cn.happyjava.springbootspringsecurityjwtmybatisplus.service.impl;

import cn.happyjava.springbootspringsecurityjwtmybatisplus.entity.AdminEntity;
import cn.happyjava.springbootspringsecurityjwtmybatisplus.mapper.AdminMapper;
import cn.happyjava.springbootspringsecurityjwtmybatisplus.security.AdminUser;
import cn.happyjava.springbootspringsecurityjwtmybatisplus.security.JwtHelp;
import cn.happyjava.springbootspringsecurityjwtmybatisplus.service.AdminService;
import cn.happyjava.springbootspringsecurityjwtmybatisplus.utils.CookiesUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author happy
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    private final JwtHelp jwtHelp;

    public AdminServiceImpl(AdminMapper adminMapper,
                            JwtHelp jwtHelp) {
        this.adminMapper = adminMapper;
        this.jwtHelp = jwtHelp;
    }

    @Override
    public AdminUser login(HttpServletResponse response, String username, String password) {
//        QueryWrapper<AdminEntity> queryWrapper = Wrappers.<AdminEntity>emptyWrapper()
//                .eq("username", username)
//                .eq("password", password);
        QueryWrapper<AdminEntity> queryWrapper = Wrappers.<AdminEntity>query()
                .eq("username", username)
                .eq("password", password);
        AdminEntity adminEntity = adminMapper.selectOne(queryWrapper);
        if (adminEntity == null) {
            // 采用异常流处理业务逻辑
            throw new RuntimeException("用户名密码错误");
        }
        AdminUser adminUser = AdminUser.parse(adminEntity);
        String token = jwtHelp.generateToken(adminUser);
        // 这里不一定是采用cookie，如果是移动端，可以直接返回
        CookiesUtils.setCookies(response, "token", token);
        return adminUser;
    }
}
