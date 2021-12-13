package nextstep.subway;

import nextstep.subway.path.domain.JgraphtPathFinder;
import nextstep.subway.path.domain.PathFinder;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
  @Bean
  public PathFinder pathFinder() {
    return new JgraphtPathFinder(new WeightedMultigraph<>(DefaultWeightedEdge.class));
  }
}
