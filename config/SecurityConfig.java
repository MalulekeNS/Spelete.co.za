package com.spelete.shop.config;
import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
  @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf->csrf.disable()).cors(Customizer.withDefaults())
      .authorizeHttpRequests(auth->auth
        .requestMatchers("/api/auth/**","/api/products/**","/api/contact").permitAll()
        .anyRequest().authenticated())
      .formLogin(form->form.disable()).httpBasic(basic->basic.disable())
      .logout(l->l.logoutUrl("/api/auth/logout").logoutSuccessHandler((req,res,auth)->res.setStatus(200)));
    return http.build();
  }
}
