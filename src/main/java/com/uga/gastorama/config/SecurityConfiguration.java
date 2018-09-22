package com.uga.gastorama.config;

import com.uga.gastorama.security.*;
import com.uga.gastorama.security.jwt.*;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService, TokenProvider tokenProvider, CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/h2-console/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
        .and()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            
            //commands
            .antMatchers(HttpMethod.GET,"/api/commands/**").authenticated()
            .antMatchers(HttpMethod.POST,"/api/commands/**").authenticated()
            .antMatchers(HttpMethod.PUT,"/api/commands/**").authenticated()
            .antMatchers(HttpMethod.DELETE,"/api/commands/**").authenticated()
            //command-lines
            //.antMatchers(HttpMethod.GET,"/api/command-lines/**").authenticated()
            .antMatchers(HttpMethod.GET,"/api/command-lines/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/command-lines/**").authenticated()
            .antMatchers(HttpMethod.PUT,"/api/command-lines/**").authenticated()
            .antMatchers(HttpMethod.DELETE,"/api/command-lines/**").authenticated()
            //confectioner
            .antMatchers(HttpMethod.GET,"/api/confectioners/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/confectioners/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.PUT,"/api/confectioners/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.DELETE,"/api/confectioners/**").access("hasRole('ROLE_ADMIN')")
            //confectioner photo
            .antMatchers(HttpMethod.GET,"/api/confectioner-photos/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/confectioner-photos/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            .antMatchers(HttpMethod.PUT,"/api/confectioner-photos/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            .antMatchers(HttpMethod.DELETE,"/api/confectioner-photos/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            //product
            .antMatchers(HttpMethod.GET,"/api/products/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/products/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            .antMatchers(HttpMethod.PUT,"/api/products/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            .antMatchers(HttpMethod.DELETE,"/api/products/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            //product-types
            .antMatchers(HttpMethod.GET,"/api/product-types/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/product-types/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.PUT,"/api/product-types/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.DELETE,"/api/product-types/**").access("hasRole('ROLE_ADMIN')")
            //ingredients
            .antMatchers(HttpMethod.GET,"/api/ingredients/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/ingredients/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            .antMatchers(HttpMethod.PUT,"/api/ingredients/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            .antMatchers(HttpMethod.DELETE,"/api/ingredients/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            //product-photos
            .antMatchers(HttpMethod.GET,"/api/product-photos/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/product-photos/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            .antMatchers(HttpMethod.PUT,"/api/product-photos/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            .antMatchers(HttpMethod.DELETE,"/api/product-photos/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_PARTNER')")
            //product-type-photos
            .antMatchers(HttpMethod.GET,"/api/product-type-photos/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/product-type-photos/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.PUT,"/api/product-type-photos/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.DELETE,"/api/product-type-photos/**").access("hasRole('ROLE_ADMIN')")            
             //user
            .antMatchers(HttpMethod.GET,"/api/users/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.POST,"/api/users/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.PUT,"/api/users/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.DELETE,"/api/users/**").access("hasRole('ROLE_ADMIN')")
            
            .antMatchers("/api/**").authenticated()
            .antMatchers("/websocket/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/websocket/**").permitAll()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .apply(securityConfigurerAdapter());

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
