package com.pefthymiou.mars.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pefthymiou.mars.security.AuthenticatorSecurityContextHolder;
import com.pefthymiou.mars.security.JwtAuthenticationFilter;
import com.pefthymiou.mars.security.JwtAuthorizationFilter;
import com.pefthymiou.mars.security.JwtTokenGenerator;
import com.pefthymiou.mars.security.ratelimit.IpRateLimitFilter;
import com.pefthymiou.mars.security.ratelimit.RequestsWindow;
import com.pefthymiou.mars.user.domain.actions.login.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_COLONIST = "COLONIST";

    @Value("${api.basepath}")
    private String apiBasepath;
    @Value("${api.limit.window}")
    private int window;
    @Value("${api.limit.threshold}")
    private int threshold;

    private JwtTokenGenerator jwtTokenGenerator;
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticatorSecurityContextHolder authenticatorSecurityContextHolder;

    @Autowired
    public WebSecurityConfiguration(JwtTokenGenerator jwtTokenGenerator, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticatorSecurityContextHolder authenticatorSecurityContextHolder) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticatorSecurityContextHolder = authenticatorSecurityContextHolder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, apiBasepath + "/unit/{id}").hasAnyAuthority(ROLE_COLONIST, ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, apiBasepath + "/unit").hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, apiBasepath + "/unit/{id}").hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.PATCH, apiBasepath + "/unit/{id}").hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, apiBasepath + "/booking").hasAnyAuthority(ROLE_COLONIST, ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, apiBasepath + "/units/browse").hasAnyAuthority(ROLE_COLONIST, ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, apiBasepath + "/units").hasAnyAuthority(ROLE_COLONIST, ROLE_ADMIN)
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenGenerator, new ObjectMapper()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtTokenGenerator, authenticatorSecurityContextHolder))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Scope(value="singleton")
    @Order()
    public RequestsWindow requestsWindow() {
        return new RequestsWindow(window, threshold);
    }

    @Bean
    @Order()
    public IpRateLimitFilter ipRateLimitFilter(RequestsWindow requestsWindow) {
        return new IpRateLimitFilter(requestsWindow);
    }

    @Bean
    @Order()
    public FilterRegistrationBean ipRateLimitFilterRegistration(IpRateLimitFilter ipRateLimitFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(ipRateLimitFilter);
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
