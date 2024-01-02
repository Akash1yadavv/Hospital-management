/**
 * 
 */
package com.hospital.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hospital.config.oauth.oauth2.CustomOAuth2UserService;
import com.hospital.config.oauth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.hospital.config.oauth.oauth2.OAuth2AuthenticationFailureHandler;
import com.hospital.config.oauth.oauth2.OAuth2AuthenticationSuccessHandler;

/**
 * @author Ankit Patel
 *
 */
@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired private AuthenticationProvider authenticationProvider;
    @Autowired private JwtValidatorFilter jwtValidatorFilter;

    @Autowired private CustomOAuth2UserService customOAuth2UserService;

    @Autowired private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    
    
    
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                	.requestMatchers("/v3/api-docs/**", "/swagger-ui/**","/swagger-ui.html", "/api-docs", "/whois", "/whois/**").permitAll()
                	.requestMatchers("/auth/**","/ccavenue/payment-handler-redirect","/").permitAll()
//                    .requestMatchers("/users/").authenticated()
                    .requestMatchers("/domain/search","/domain/priceForDomain").permitAll()
                    .requestMatchers("/appointments/direct-book","/appointments/book/**").permitAll()
                    .requestMatchers("/appointments/mobile/**","/appointments/mobile-aadhar/**").permitAll()
                    .requestMatchers("/appointments/email/**","/appointments/email-mobile/**").permitAll()
                    .requestMatchers("/appointments/download-invoice/**").permitAll()
                    .requestMatchers("/departments/by-category/**","/departments/all/**").permitAll()
                    .requestMatchers("/categories/all/**", "/slots/available-slots/**").permitAll() 
                    .requestMatchers("/contacts/add/**", "/contacts/all/**").permitAll() 
                .anyRequest().hasAnyAuthority("ROLE_ADMIN")
//                    .requestMatchers("/test/**").permitAll()
//                    .anyRequest().permitAll()
//                   	.anyRequest().authenticated()

                ).addFilterBefore(jwtValidatorFilter, UsernamePasswordAuthenticationFilter.class)
//                .oauth2Login(Customizer.withDefaults())
                .oauth2Login(auth2 -> auth2.authorizationEndpoint(endpoints -> endpoints.baseUri("/oauth2/authorize").authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                		
                		.redirectionEndpoint(redirectEndpoint-> redirectEndpoint.baseUri("/oauth2/callback/*"))
                		.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                		.successHandler(oAuth2AuthenticationSuccessHandler)
                		.failureHandler(oAuth2AuthenticationFailureHandler)
//                		.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)

                		)
                .exceptionHandling((ex)->{
                	ex.authenticationEntryPoint(new Http403ForbiddenEntryPoint());
                })

                
                ;
        return http.build();
    }
	
	
	

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(authenticationProvider);


        return new ProviderManager(authenticationProviders);
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource(@Value("${allowedOrigins}") String allowedOriginsString) {
    	List<String> allowedOriginsList = Arrays.asList(allowedOriginsString.split(","));
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOriginsList);
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type","Content-type","*"));
        configuration.setExposedHeaders(Arrays.asList("*","X-Get-Header"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins("http://localhost:4200")
//                        .allowCredentials(Boolean.TRUE);
//            }
//        };
//    }

}
