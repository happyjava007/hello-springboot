## Spring Security介绍

Spring Security是Spring全家桶中的处理身份和权限问题的一员。Spring Security可以根据使用者的需要定制相关的角色身份和身份所具有的权限，完成黑名单操作、拦截无权限的操作等等。

本文将讲解Springboot中使用spring security。



## 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

由于SpringBoot的自动配置的特性，引入了Spring Security依赖之后，已经默认帮我们配置了。不信，现在访问应用的根目录：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43ce02a8a68?w=1495&h=557&f=png&s=57662)

居然跳到了一个登陆页面，我们什么都没有写呀。我们把spring security的依赖去掉，重启应用，然后再次访问根目录：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43ce0115f52?w=804&h=349&f=png&s=28127)

这次，熟悉的页面出来了。其实这就是Springboot的魅力之处——自动配置。我们只是引入了Spring Security的依赖，就自动帮我们配置完了。



## 默认账号密码

我们可以通过SecurityProperties的源码查看，其默认账号是user，密码是启动的时候随机生成的，可以在日志里找到：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43ce03ed406?w=821&h=397&f=png&s=41903)

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43ce0cfc49f?w=918&h=152&f=png&s=33808)



## 修改默认账号密码

我们可以通过配置文件修改Spring Security的默认账号密码，如下：

```properties
spring.security.user.name=happyjava
spring.security.user.password=123456
```

springboot1.x版本为：

```properties
security.user.name=admin
security.user.password=admin
```

如果是一个普通的个人网站，如个人博客等。配置到这一步，已经可以充当一个登陆控制模块来使用了（当然还需要配置不需要登陆就可以访问的url）。



## 使用Spring Security定制化鉴权模块

虽然默认已经帮我们实现了一个简单的登陆认证模块，但是在实际开发中，这还是远远不够的。比如，我们有多个用户，有多中角色等等。一切，还是需要手动来开发。下面就一步一步来使用Spring Security：



#### 配置不需要登陆的路径

我们当然需要配置不需要登陆就能访问的路径啦，比如：登陆接口（不然你怎么访问）。



有如下接口：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43ce169db95?w=665&h=372&f=png&s=35640)



新建SecurityConfig，然后配置拦截的路径，配置路径白名单：

```java
/**
 * @author Happy
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/notneedlogin/**").permitAll()
                .anyRequest().authenticated();
    }


}
```

排版乱请看图片版

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43ce18bc249?w=868&h=481&f=png&s=66462)

通过antMatchers("url").permitAll()方法，配置了/api/notneedlogin/**路径会被Spring Security放行。



启动项目验证下：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d1aafd43f?w=811&h=395&f=png&s=28968)

需要登陆的接口拦截了返回403.

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d1abe93b6?w=531&h=196&f=png&s=9891)

配置了白名单的路径成功的获取到了数据。

其实，这个时候已经可以拿来当做一个普通个人网站的权限验证模块了，比如个人博客什么的。



## 抛弃默认配置，自定义鉴权方式

很多时候，我们都需要自定义鉴权方式啦。比如，我不用session来鉴权了，改用无状态的jwt方式（json web token）。这时候，我们就要对Spring Security进行定制化了。

首先，我们需要创建UserDetails的实现，这个就是Spring Security管理的用户权限对象：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d1aef7aa6?w=522&h=226&f=png&s=19044)

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser implements UserDetails {

    private String username;

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 这里可以定制化权限列表
        return Collections.EMPTY_LIST;
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
        // 这里设置账号是否已经过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 这里设置账号是否已经被锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 这里设置凭证是否过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 是否可用
        return true;
    }
}

```



其次，我们还需要一个过滤器，拦截请求判断是否登陆，组装UserDetails：

#### AuthFilter.class

```java
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username != null && !"".equals(username)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails != null && userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                // 把当前登陆用户放到上下文中
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                        request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // 用户不合法，清除上下文
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

}
```

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d1cd6eabd?w=1147&h=695&f=png&s=114583)



这个过滤器里的userDetailsService，是Spring Security加载UserDetails的一个接口，代码如下：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d2229ba48?w=994&h=174&f=png&s=21968)

它只有一个根据用户名加载当前权限用户的方法，我的实现如下：

```java
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
```

MOCK这里，需要用户真正的去实现，我这里只是演示使用。其中AdminUserEntity代码如下：

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserEntity {

    private Integer id;

    private String username;

    private String password;

}
```



到这里，已经完成了Spring Security的整套配置了。



## 测试

下面通过三个接口，测试配置是否生效：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d34164dc7?w=866&h=567&f=png&s=76011)

增加了一个登陆接口，模拟真实用户登陆。其中，needLogin接口，使用了AuthenticationPrincipal注解来获取Spring Security中上下文的用户（这个实在Filter里面设置的）。



未登陆状态，访问test1接口：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d493146c5?w=511&h=455&f=png&s=22510)

直接被拦截掉了，调用登录接口：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d50397087?w=522&h=331&f=png&s=14070)

再次访问：

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d571fd27e?w=497&h=478&f=png&s=22927)

成功请求到了接口。



## 无状态jwt鉴权

本文演示的是使用session来完成鉴权的。使用session来做登录凭证，一个很大的痛点就是session共享问题。虽然springboot解决session共享就几个配置的问题，但终究还是得依赖别的服务，这是有状态的。

现在流行一种使用加密token的验证方式来鉴权，本人在项目中也是使用token的方式的(jjwt)。其主要做法就是，用户调用登陆接口，返回一串加密字符串，这串字符串里面包含用户名(username)等信息。用户后续的请求，把这个token带过来，通过解密的方式验证用户是否拥有权限。

![](https://user-gold-cdn.xitu.io/2019/6/21/16b7a43d5bbe6ef5?w=1027&h=298&f=png&s=64702)



## 总结

本文讲解了使用Spring Security来做鉴权框架，Spring Security配置起来还是挺繁琐的，但是配置完成之后，后续的获取上下文用户注解什么的，是真的方便。我把代码放到GitHub上，方便大家下载直接复制使用，地址如下：<https://github.com/Happy4Java/SpringSecurityDemo>

