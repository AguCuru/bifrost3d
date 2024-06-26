package com.apc.bifrost3d;

import com.apc.bifrost3d.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurity {

  @Autowired
  private UserService userService;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService)
        .passwordEncoder(new BCryptPasswordEncoder());
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/admin/*").hasRole("ADMIN")
            //.requestMatchers("/user/*").hasRole("USER")
            .requestMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()

        )
        .formLogin((form) -> form
            .loginPage("/auth/login")
            .loginProcessingUrl("/logincheck")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/")
            .permitAll())
        .logout((logout) -> logout
            .logoutUrl("/auth/logout")
            .logoutSuccessUrl("/auth/login")
            .permitAll())
        .csrf((csrf) -> csrf.disable());

    return http.build();
  }

}
