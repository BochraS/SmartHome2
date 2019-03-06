package com.zsoft.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.zsoft.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.zsoft.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.zsoft.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.zsoft.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.zsoft.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.zsoft.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(com.zsoft.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(com.zsoft.domain.SmartObject.class.getName(), jcacheConfiguration);
            cm.createCache(com.zsoft.domain.SmartObject.class.getName() + ".configs", jcacheConfiguration);
            cm.createCache(com.zsoft.domain.Config.class.getName(), jcacheConfiguration);
            cm.createCache(com.zsoft.domain.Config.class.getName() + ".confValues", jcacheConfiguration);
            cm.createCache(com.zsoft.domain.ConfValue.class.getName(), jcacheConfiguration);
            cm.createCache(com.zsoft.domain.ProfileConfValue.class.getName(), jcacheConfiguration);
            cm.createCache(com.zsoft.domain.ProfileConfValue.class.getName() + ".configs", jcacheConfiguration);
            cm.createCache(com.zsoft.domain.ProfileConfValue.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.zsoft.domain.Profile.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
