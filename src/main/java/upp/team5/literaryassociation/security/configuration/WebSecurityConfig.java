package upp.team5.literaryassociation.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import upp.team5.literaryassociation.security.service.CustomUserDetailsService;
import upp.team5.literaryassociation.security.auth.RestAuthenticationEntryPoint;
import upp.team5.literaryassociation.security.token.TokenAuthenticationFilter;
import upp.team5.literaryassociation.security.token.TokenUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    TokenUtils tokenUtils;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Definisemo nacin autentifikacije
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    // Definisemo prava pristupa odredjenim URL-ovima
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

                .authorizeRequests()
//                .antMatchers("/").hasAnyAuthority("ADMIN", "...")
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/camunda/**").permitAll()
                .antMatchers("/camunda*").permitAll()
                .anyRequest().authenticated()
                .and()

                .cors().and()

                .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, customUserDetailsService),
                        BasicAuthenticationFilter.class);

        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // TokenAuthenticationFilter ce ignorisati sve ispod navedene putanje
        web.ignoring().antMatchers(HttpMethod.POST, "/auth/**");

        web.ignoring().antMatchers(HttpMethod.GET, "/camunda-welcome");
        web.ignoring().antMatchers(HttpMethod.GET, "/camunda/**");
        web.ignoring().antMatchers(HttpMethod.POST, "/camunda/**");
        web.ignoring().antMatchers(HttpMethod.PUT, "/camunda/**");
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/camunda/**");
        web.ignoring().antMatchers(HttpMethod.DELETE, "/camunda/**");
        web.ignoring().antMatchers(HttpMethod.HEAD, "/camunda/**");
        web.ignoring().antMatchers(HttpMethod.PATCH, "/camunda/**");
    }
}
