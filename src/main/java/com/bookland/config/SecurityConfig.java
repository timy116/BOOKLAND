package com.bookland.config;

import com.bookland.Google.GoogleOauth2UserService;
import com.bookland.facebook.FacebookConnectionSignup;
import com.bookland.facebook.FacebookSignInAdapter;
import com.bookland.handler.CustomSavedRequestAwareAuthenticationSuccessHandler;
import com.bookland.handler.Oauth2LoginSuccessHandler;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static String PREFIX = "prod";

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

    @Value("${spring.social.facebook.appSecret:}")
    String appSecret;

    @Value("${spring.social.facebook.appId:}")
    String appId;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 設定 CSS 防護
        http
                .headers()
                .xssProtection();

        http
                .csrf()
                .ignoringAntMatchers("/linebot/**")
                .and()
                .authorizeRequests()
                .antMatchers("/static/**")
                .permitAll()
                .antMatchers("/book/**")
                .permitAll()
                .antMatchers("/", "/about", "/login", "/inspect", "/search/**", "/cart/**", "/message")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/register", "/login", "/cart/**", "/oauth2/**")
                .permitAll()
                // facebook
                .antMatchers(HttpMethod.POST, "/signin/**", "/signup/**", "/linebot/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/signin/**", "/signup/**")
                .permitAll()
                // google
                .antMatchers("/oauth2/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(googleOauth2UserService)
                .and()
                .successHandler(oauth2LoginSuccessHandler)
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
                .logout()
                .logoutSuccessUrl("/")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));
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

    @Autowired
    private Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;

    //    @Bean
//    public  Oauth2LoginSuccessHandler oauth2LoginSuccessHandler(){
//        return new Oauth2LoginSuccessHandler();
//    }
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


    @Autowired
    private GoogleOauth2UserService googleOauth2UserService;

    // 使用 Jdbc 當作 repository
    private UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    }
}
