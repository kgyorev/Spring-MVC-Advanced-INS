package com.insurance.ins.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by K on 15.3.2018 г..
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] PERMITTED_ROUTES =
            {"/","/login", "/register", "/css/**","/js/**","/img/**"};

    @Qualifier("userServiceImpl")
    @Autowired
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(@Qualifier("userServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(PERMITTED_ROUTES).permitAll()
                .anyRequest().authenticated()
                .and()
//                .csrf().disable()
                .formLogin()
                .loginPage("/login").permitAll().failureUrl("/login?error=true")
                .defaultSuccessUrl("/")
                .passwordParameter("password")
                .usernameParameter("username")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling().accessDeniedPage("/unauthorized")
                .and().userDetailsService(this.userDetailsService)
        ;

    }
}
