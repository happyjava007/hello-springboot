package cn.happyjava.springbootspringsecurityjwtmybatisplus.security;

import cn.happyjava.springbootspringsecurityjwtmybatisplus.utils.CookiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Happy
 */
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtHelp jwtHelp;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String token = CookiesUtils.getValue(request, "token");
        String username = null;
        if (StringUtils.isNotBlank(token)) {
            username = jwtHelp.getUsernameFromToken(token);
        }
        if (!StringUtils.isBlank(username)) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } catch (UsernameNotFoundException e) {
                log.error("username【{}】 not found", username, e);
            }
        } else {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}
