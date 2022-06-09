package com.example.register.config;

import com.example.register.filter.AuthenticationFilter;
import com.example.register.filter.AuthorizationFilter;
import com.example.register.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
   // private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder)    {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = (UserDetailsServiceImpl) userDetailsService;
    }*/

        // Authentication : User --> Roles
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

            /*auth.inMemoryAuthentication()
                    .passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
                    .withUser("user1").password("secret1").roles("USER")
                    .and()
                    .withUser("admin1").password("secret1").roles("USER", "ADMIN");*/
        }

        // Authorization : Role -> Access
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().headers().frameOptions().disable().and()
                     .httpBasic().and()
                    .authorizeRequests()
                    .antMatchers("/login","/registerUser","/verifyEmail","/verifyOtp").permitAll()
                    .antMatchers("/user").hasRole("USER")
                    .antMatchers("/users").hasRole("ADMIN")
                    .and()
                    .addFilter(new AuthenticationFilter(authenticationManager()))
                    .addFilter(new AuthorizationFilter(authenticationManager()))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

    }
