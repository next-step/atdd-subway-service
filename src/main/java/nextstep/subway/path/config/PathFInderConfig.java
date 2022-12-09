package nextstep.subway.path.config;

import nextstep.subway.path.application.DijkstraPathFinder;
import nextstep.subway.path.application.KShortestPathFinder;
import nextstep.subway.path.application.PathFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PathFInderConfig {
    @Primary
    @Bean
    PathFinder getDijkstraPathFinder(){
        return new DijkstraPathFinder();
    }

    @Bean
    PathFinder getKShortestPathFinder(){
        return new KShortestPathFinder();
    }
}
