package com.sky.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 创建并配置一个 ObjectMapper 对象，用于处理 JSON 数据的序列化和反序列化
     * 该 ObjectMapper 注册了一个 JavaTimeModule，用于处理 LocalDateTime 类型的序列化和反序列化
     * 同时配置为在反序列化时忽略未知属性
     *
     * @return 配置好的 ObjectMapper 对象
     */
    @Bean
    public ObjectMapper objectMapper() {
        //--------------------------------时间格式校准------------------------------------------
        // 创建一个 JavaTimeModule 对象
        JavaTimeModule module = new JavaTimeModule();
        // 为 LocalDateTime 类型添加一个序列化器，使用指定的格式化器
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        // 为 LocalDateTime 类型添加一个反序列化器，使用指定的格式化器
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        // 创建一个新的 ObjectMapper 对象，并注册上面创建的 JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper().registerModule(module);

        //--------------------------------反序列化时忽略未知属性：只封装需要的属性，丢弃不必要的属性------------------------------------------
        // 配置 ObjectMapper，使其在反序列化时忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 返回配置好的 ObjectMapper 对象
        return objectMapper;
    }
}
