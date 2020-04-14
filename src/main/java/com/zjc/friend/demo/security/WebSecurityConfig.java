//package com.zjc.friend.demo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.session.SessionRegistryImpl;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
//import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
//
//    @Autowired
//    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
//
//    @Bean
//    public SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }
//
//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//        tokenRepository.setDataSource(dataSource);
//        // 如果token表不存在，使用下面语句可以初始化该表；若存在，会报错。
//        //tokenRepository.setCreateTableOnStartup(true);
//        return tokenRepository;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable();
//        http
//                //使用form表单post方式进行登录
//                .formLogin()
//                //登录页面为自定义的登录页面
//                .loginPage("/login")
//                //设置登录成功跳转页面，error=true控制页面错误信息的展示
//                .successForwardUrl("/user-dynamic/allDynamicInfo").failureUrl("/login?error=true")
//                .permitAll()
//                .and()
//                .headers().frameOptions().disable()
//                .and()
//                //允许不登陆就可以访问的方法，多个用逗号分隔
//                .authorizeRequests().antMatchers("/userinfo/*", "/login","/registerInfo", "/websocket/*").permitAll()
//                //其他的需要授权后访问
//                .anyRequest().authenticated();
//
//        //session管理,失效后跳转
//        http.sessionManagement().invalidSessionUrl("/login");
//        //单用户登录，如果有一个登录了，同一个用户在其他地方登录将前一个剔除下线
//        http.sessionManagement().
//                maximumSessions(1)
//                .expiredSessionStrategy(new CustomExpiredSessionStrategy())
//                .sessionRegistry(sessionRegistry())
//                .maxSessionsPreventsLogin(true);
//        //单用户登录，如果有一个登录了，同一个用户在其他地方不能登录
////        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
//        //退出时情况cookies
//        http.logout().deleteCookies("JESSIONID");
//        //解决中文乱码问题
//        CharacterEncodingFilter filter = new CharacterEncodingFilter();
//        filter.setEncoding("UTF-8");
//        filter.setForceEncoding(true);
//        //
//        http.addFilterBefore(filter, CsrfFilter.class);
//    }
//
//    /**
//     * 静态资源忽略
//     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers( "/static/**","/templates/**","/friend/**","/static/manager/**");
//    }
//
//    class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
//        @Override
//        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//            response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
//        }
//    }
//}
