package cn.happyjava.springbootspringsecurityjwtmybatisplus.security;

import cn.happyjava.springbootspringsecurityjwtmybatisplus.entity.AdminEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Happy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Scope("session")
public class AdminUser implements UserDetails {

    private String username;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static AdminUser parse(AdminEntity adminEntity) {
        return new AdminUser(adminEntity.getUsername());
    }

}
