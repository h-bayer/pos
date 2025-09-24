//package de.bayer.pharmacy.edgeposapi.config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//@Configuration
//public class SecurityConfig {
//  @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http.csrf(csrf -> csrf.disable());
//    http.authorizeHttpRequests(auth -> auth
//      .requestMatchers("/actuator/**","/swagger-ui.html","/swagger-ui/**","/v3/api-docs/**").permitAll()
//      .anyRequest().authenticated());
//    http.httpBasic(Customizer.withDefaults());
//    return http.build();
//  }
//  @Bean UserDetailsService users() {
//    return new InMemoryUserDetailsManager(
//      User.withUsername("pharmacist").password("{noop}pharmacist").roles("PHARMACIST").build(),
//      User.withUsername("assistant").password("{noop}assistant").roles("ASSISTANT").build(),
//      User.withUsername("admin").password("{noop}admin").roles("ADMIN").build()
//    );
//  }
//}
