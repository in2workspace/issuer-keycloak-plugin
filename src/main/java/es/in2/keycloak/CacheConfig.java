package es.in2.keycloak;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class CacheConfig {

    @Produces
    public CacheStore<String> cacheStoreForString() {
        return new CacheStore<>(10, TimeUnit.MINUTES);
    }
}