package com.greatlearning.week13.apicore.config;

import com.greatlearning.week13.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Prevents auto generation of session password
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Bean
  public UserDetailsService userDetailsService() {
      return new UserDetailsServiceImpl();
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
    @Bean
    PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


      http.csrf().disable().authorizeRequests()
              .antMatchers("/publish/user","/usertouser").hasAnyAuthority("USER")
              .antMatchers("/publish/admin").hasAnyAuthority("ADMIN")
              .antMatchers("/").permitAll().and().formLogin()
              .and()
              .logout().permitAll()
              ;


    }
}
