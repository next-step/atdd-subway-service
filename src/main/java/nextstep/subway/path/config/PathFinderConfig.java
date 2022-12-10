package nextstep.subway.path.config;

import nextstep.subway.path.application.DijkstraPathFinder;
import nextstep.subway.path.application.KShortestPathFinder;
import nextstep.subway.path.application.PathFindAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PathFinderConfig {

    @Primary
    @Bean
    public PathFindAlgorithm getDijkstraPathFinder() {
        return new DijkstraPathFinder();
    }

    @Bean
    public PathFindAlgorithm getKShortestPathFinder() {
        return new KShortestPathFinder();
    }
}
