package com.hospital.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;



@Configuration
public class Beans {
	@Value("${spring.mail.templates.path}")
	private String mailTemplatesPath;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean 
    RestTemplate geRestTemplate() {
    	return new RestTemplate();
    }
    
    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF\n" +
                "ROLE_STAFF > ROLE_USER\n" +
                "ROLE_USER > ROLE_GUEST"
                );
        return hierarchy;
    }

    // and, if using method security also add
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
    	DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    	expressionHandler.setRoleHierarchy(roleHierarchy);
    	return expressionHandler;
    }
    
    
    @Bean
    @Primary
    ITemplateResolver thymeleafTemplateResolver() {
    	FileTemplateResolver templateResolver = new FileTemplateResolver();
    	templateResolver.setPrefix(mailTemplatesPath);
    	
//    	ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setPrefix("mail-templates/");

        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }
    
    @Bean
    ResourceBundleMessageSource emailMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mailMessages");
        return messageSource;
    }
    
    @Bean
    SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }
    
    
//    @Bean
//    PathPatternParser pathPatternParser() {
//        PathPatternParser parser = new PathPatternParser();
//        // Configure the parser to ignore trailing slashes
//        parser.setMatchOptionalTrailingSeparator(true);
//        return parser;
//    }
//    
    
    
    @Bean
    @Primary
    RedisCacheConfiguration defaultCacheConfig(ObjectMapper objectMapper) {
    	objectMapper = objectMapper.copy();

        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

    	
        return RedisCacheConfiguration.defaultCacheConfig()
        		.entryTtl(Duration.ofMinutes(1))
        		.disableCachingNullValues()
            .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
    }
    
    
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
//            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//            //objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//
//            RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//
//            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
//                    .entryTtl(Duration.ofMinutes(1));
//
//            return RedisCacheManager.builder(redisConnectionFactory)
//                    .cacheDefaults(defaultRedisCacheConfiguration())
//                    .build();
//    }
    
}
