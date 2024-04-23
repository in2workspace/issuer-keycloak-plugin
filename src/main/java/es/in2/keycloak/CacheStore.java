package es.in2.keycloak;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.concurrent.TimeUnit;

@Dependent
public class CacheStore<T> {

    private Cache<String, T> cache;

    @Inject
    public CacheStore(
            @ConfigProperty(name = "cache.expiry.duration") long expiryDuration,
            @ConfigProperty(name = "cache.expiry.unit") TimeUnit timeUnit) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public T get(String key) {
        return cache.getIfPresent(key);
    }

    public void delete(String key) {
        cache.invalidate(key);
    }

    public String add(String key, T value) {
        if (key != null && !key.trim().isEmpty() && value != null) {
            cache.put(key, value);
            return key;
        }
        return null;
    }
}
//@Dependent
//public class CacheStore<T> {
//
//    private Cache<String, T> cache;
//
//    public CacheStore(long expiryDuration, TimeUnit timeUnit) {
//        this.cache = CacheBuilder.newBuilder()
//                .expireAfterWrite(expiryDuration, timeUnit)
//                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
//                .build();
//    }
//
//    public T get(String key) {
//        return cache.getIfPresent(key);
//    }
//
//    public void delete(String key) {
//        cache.invalidate(key);
//    }
//
//    public String add(String key, T value) {
//        if (key != null && !key.trim().isEmpty() && value != null) {
//            cache.put(key, value);
//            return key;
//        }
//        return null;
//    }
//}