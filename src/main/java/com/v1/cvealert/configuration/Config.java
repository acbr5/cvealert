package com.v1.cvealert.configuration;

import com.v1.cvealert.error.MyAccessDeniedHandler;
import com.v1.cvealert.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // For ADMIN only.
        //  http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");

        // When the user has logged in as XX.
        // But access a page that requires role YY,
        // AccessDeniedException will be thrown.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        http
                .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS,"*/").permitAll()
                .antMatchers(HttpMethod.GET,"/").permitAll()
                .antMatchers("/css/**", "/", "/account/**").permitAll()
                .antMatchers("/admin").authenticated()
                .antMatchers("/reports").authenticated()
                .antMatchers("/account/**").authenticated()
                .antMatchers("/login*").permitAll()
                .antMatchers("/anonymous*", "/profile/**").anonymous()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .and()
                .exceptionHandling().accessDeniedPage("/errors/403.html")
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .deleteCookies("JSESSIONID");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new MyAccessDeniedHandler();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
}