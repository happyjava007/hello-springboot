package cn.happyjava.springbootspringsecurityjwtmybatisplus.security;

import cn.happyjava.springbootspringsecurityjwtmybatisplus.entity.AdminEntity;
import cn.happyjava.springbootspringsecurityjwtmybatisplus.mapper.AdminMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Loger
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminMapper adminMapper;

    public UserDetailsServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<AdminEntity> queryWrapper = Wrappers.<AdminEntity>query()
                .eq("username", username);
        AdminEntity adminEntity = adminMapper.selectOne(queryWrapper);
        if (adminEntity == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return AdminUser.parse(adminEntity);
    }
}
