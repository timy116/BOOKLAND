package com.bookland.config;

import com.bookland.facebook.FacebookConnectionSignup;
import com.bookland.facebook.FacebookSignInAdapter;
import com.bookland.handler.CustomSavedRequestAwareAuthenticationSuccessHandler;
import com.bookland.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private CustomSavedRequestAwareAuthenticationSuccessHandler customSavedRequestAwareAuthenticationSuccessHandler;

    @Autowired
    FacebookConnectionSignup facebookConnectionSignup;

    @Autowired
    FacebookSignInAdapter facebookSignInAdapter;

    @Value("${spring.social.facebook.appSecret}")
    String appSecret;

    @Value("${spring.social.facebook.appId}")
    String appId;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/static/**")
                .permitAll()
                .antMatchers("/book/**")
                .permitAll()
                .antMatchers("/", "/login", "/inspect")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/register", "/login")
                .permitAll()
                // facebook
                .antMatchers("/signin/**", "/signup/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .successHandler(customSavedRequestAwareAuthenticationSuccessHandler)
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
//                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository)
                .userDetailsService(userDetailService)
                .and()
                .logout().logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public CustomSavedRequestAwareAuthenticationSuccessHandler customSavedRequestAwareAuthenticationSuccessHandler() {
        return new CustomSavedRequestAwareAuthenticationSuccessHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public ProviderSignInController providerSignInController() {
        ConnectionFactoryLocator connectionFactoryLocator = connectionFactoryLocator();
        UsersConnectionRepository usersConnectionRepository = getUsersConnectionRepository(connectionFactoryLocator);
        ((JdbcUsersConnectionRepository) usersConnectionRepository).setConnectionSignUp(facebookConnectionSignup);
        return new ProviderSignInController(
                connectionFactoryLocator, usersConnectionRepository, facebookSignInAdapter
        );
    }

    @Bean
    SignInAdapter signInAdapter() {
        return new FacebookSignInAdapter();
    }

    private ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(appId, appSecret));
        return registry;
    }

    // 使用 Jdbc 當作 repository
    private UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    }
}
