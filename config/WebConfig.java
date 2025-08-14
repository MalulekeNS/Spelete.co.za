package com.spelete.shop.config;
import org.springframework.context.annotation.Configuration; import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override public void addCorsMappings(CorsRegistry r){ r.addMapping("/api/**")
    .allowedOrigins("http://localhost:5173","http://127.0.0.1:5173","https://yourdomain.com")
    .allowedMethods("GET","POST","PUT","DELETE","OPTIONS").allowCredentials(true); }
}
