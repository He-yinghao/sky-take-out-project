package com.sky.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    /**
     * 配置RedisTemplate以使用自定义的序列化器和反序列化器
     *
     * @param redisConnectionFactory Redis连接工厂，用于创建Redis连接
     * @return 配置好的RedisTemplate实例，用于操作Redis
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建RedisTemplate实例
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(redisConnectionFactory);

        // 设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());

        // 配置ObjectMapper以支持Java 8日期时间类型
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册Java 8日期时间模块，以便能够正确序列化和反序列化LocalDate、LocalDateTime等类型
        objectMapper.registerModule(new JavaTimeModule());
        // 禁用时间戳格式，确保日期时间以易读的格式（如ISO 8601）存储在Redis中
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 设置value的序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        template.setValueSerializer(serializer);


        // 返回配置好的RedisTemplate实例
        return template;
    }
}
