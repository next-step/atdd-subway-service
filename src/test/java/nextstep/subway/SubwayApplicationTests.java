package nextstep.subway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
@SpringBootTest
class SubwayApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext.containsBean("cacheManager")).isTrue();
        CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
        assertThat(cacheManager).isInstanceOf(ConcurrentMapCacheManager.class);
    }

}
