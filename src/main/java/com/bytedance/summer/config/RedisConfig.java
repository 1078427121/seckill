package com.bytedance.summer.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author pjl
 * @date 2019/8/27 10:41
 */
@Configuration
public class RedisConfig {

//    /*
//    使用fastjson进行序列化Order
//     */
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//        RedisTemplate<Object, Object> template = new RedisTemplate();
//        template.setConnectionFactory(redisConnectionFactory);
//        RedisSerializer rs=new GenericFastJsonRedisSerializer();
//        template.setDefaultSerializer(rs);
//        return template;
//    }
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate1(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//        RedisTemplate<Object, Object> template = new RedisTemplate();
////        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
////        redisSentinelConfiguration.setDatabase(1);
////        redisSentinelConfiguration.setMaster("127.0.0.1");
////        template.setConnectionFactory(new LettuceConnectionFactory(redisSentinelConfiguration));
//        template.setConnectionFactory(redisConnectionFactory);
//        RedisSerializer rs=new GenericFastJsonRedisSerializer();
//        template.setDefaultSerializer(rs);
//        return template;
//    }
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate2(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//        RedisTemplate<Object, Object> template = new RedisTemplate();
////        ((LettuceConnectionFactory)redisConnectionFactory).setDatabase(2);
//        template.setConnectionFactory(redisConnectionFactory);
//        RedisSerializer rs=new GenericFastJsonRedisSerializer();
//        template.setDefaultSerializer(rs);
//        return template;
//    }


    @Bean
    @ConfigurationProperties(prefix = "lettuce.pool")
    public GenericObjectPoolConfig redisPool() {
        return new GenericObjectPoolConfig<>();
    }


    @Value("${redis0.host}")
    private String host0;
    @Value("${redis0.database}")
    private int database0;
    @Value("${redis0.port}")
    private int port0;
    @Value("${redis1.host}")
    private String host1;
    @Value("${redis1.database}")
    private int database1;
    @Value("${redis1.port}")
    private int port1;
    @Value("${redis2.host}")
    private String host2;
    @Value("${redis2.database}")
    private int database2;
    @Value("${redis2.port}")
    private int port2;
    @Value("${redis3.host}")
    private String host3;
    @Value("${redis3.database}")
    private int database3;
    @Value("${redis3.port}")
    private int port3;

    @Bean
    public RedisStandaloneConfiguration redisConfig0() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(database0);
        configuration.setHostName(host0);
        configuration.setPort(port0);
        return configuration;
    }
    @Bean
    public RedisStandaloneConfiguration redisConfig1() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(database1);
        configuration.setHostName(host1);
        configuration.setPort(port1);
        return configuration;
    }

    @Bean
    public RedisStandaloneConfiguration redisConfig2() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(database2);
        configuration.setHostName(host2);
        configuration.setPort(port2);
        return configuration;
    }

    @Bean
    public RedisStandaloneConfiguration redisConfig3() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(database3);
        configuration.setHostName(host3);
        configuration.setPort(port3);
        return configuration;
    }

    @Bean("factory0")
    @Primary
    public LettuceConnectionFactory factory0(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfig0) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig0, clientConfiguration);
    }

    @Bean("factory1")
    public LettuceConnectionFactory factory1(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfig1) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig1, clientConfiguration);
    }

    @Bean("factory2")
    public LettuceConnectionFactory factory2(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfig2) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig2, clientConfiguration);
    }

    @Bean("factory3")
    public LettuceConnectionFactory factory3(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfig3) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig3, clientConfiguration);
    }

    @Bean("redisTemplate0")
    @Primary
    public RedisTemplate redisTemplate0(@Qualifier("factory0") RedisConnectionFactory factory0) {
        return getRedisTemplate(factory0);
    }

    @Bean("redisTemplate1")
    public RedisTemplate redisTemplate1(@Qualifier("factory1") RedisConnectionFactory factory1) {
        return getRedisTemplate(factory1);
    }

    @Bean("redisTemplate2")
    public RedisTemplate redisTemplate2(@Qualifier("factory2") RedisConnectionFactory factory2) {
        return getRedisTemplate(factory2);
    }

    @Bean("redisTemplate3")
    public RedisTemplate redisTemplate3(@Qualifier("factory3") RedisConnectionFactory factory3) {
        return getRedisTemplate(factory3);
    }

    private RedisTemplate getRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        RedisSerializer rs = new GenericFastJsonRedisSerializer();
        template.setDefaultSerializer(rs);
        return template;
//        RedisTemplate template = new RedisTemplate();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(RedisSerializer.string());
//        template.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
//        template.setHashKeySerializer(RedisSerializer.string());
//        template.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
//        return template;
    }
}
