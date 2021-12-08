package nextstep.subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nextstep.subway.path.domain.GraphPathFinder;
import nextstep.subway.path.domain.PathFinder;

@Configuration
public class AppConfig {

    @Bean
    public PathFinder pathFinder() {
        return new GraphPathFinder();
    }
}
