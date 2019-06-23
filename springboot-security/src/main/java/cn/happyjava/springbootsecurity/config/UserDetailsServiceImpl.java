package cn.happyjava.springbootsecurity.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // MOCK 模拟从数据库 根据用户名查询用户
        AdminUserEntity adminUser = new AdminUserEntity(1, "happyjava", "123456");
        // 构建 UserDetails 的实现类 => AdminUser
        return new AdminUser(adminUser.getUsername());
    }

}
