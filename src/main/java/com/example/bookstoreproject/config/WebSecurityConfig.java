package com.example.bookstoreproject.config;

import com.example.bookstoreproject.security.CurrentUserDetailServiceImpl;
import com.example.bookstoreproject.security.JWTAuthenticationTokenFilter;
import com.example.bookstoreproject.security.JwtAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private CurrentUserDetailServiceImpl userDetailsService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .authorizeRequests()

                .antMatchers(HttpMethod.GET, "/authors","/books", "/categories", "/cities", "/countries", "/publishers",
                        "/ratings", "/states", "/users").permitAll()
                .antMatchers(HttpMethod.POST, "/credit-card", "/membership-card").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/ratings/*").authenticated()
                .antMatchers(HttpMethod.PUT, "/categories").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/users").authenticated()
                .antMatchers(HttpMethod.DELETE, "/users/{id}", "/books/{id}", "/categories/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/ratings").authenticated();

        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }


    @Bean
    public JWTAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JWTAuthenticationTokenFilter();
    }
}