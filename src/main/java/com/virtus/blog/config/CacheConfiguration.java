package com.virtus.blog.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.virtus.blog.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.virtus.blog.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.Post.class.getName(), jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.Post.class.getName() + ".commentaries", jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.Commentary.class.getName(), jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.Body.class.getName(), jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.Body.class.getName() + ".assets", jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.Asset.class.getName(), jcacheConfiguration);
            cm.createCache(com.virtus.blog.domain.AuthorAcess.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
