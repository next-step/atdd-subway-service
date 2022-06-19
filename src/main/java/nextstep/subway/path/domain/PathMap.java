package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathMap {
    public PathMap() {
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> createMap(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.forEach(
                line -> line.getSections()
                            .getSections()
                            .forEach(
                                    section -> {
                                        graph.addVertex(section.getUpStation());
                                        graph.addVertex(section.getDownStation());
                                        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
                                    }
                            ));
        return graph;
    }
}
