package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.exception.CannotReachableException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Optional;

import static java.lang.String.format;

public class PathGraph extends WeightedMultigraph<Station, DefaultWeightedEdge> {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public PathGraph(Sections sections) {
        super(DefaultWeightedEdge.class);

        validateConstructor(sections);
        this.shortestPath = setUpGraph(sections);
    }

    private void validateConstructor(Sections sections) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("전달 받은 구간이 유효하지 않습니다.");
        }
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> setUpGraph(Sections sections) {
        sections.get()
                .forEach(section -> {
                    this.addVertex(section.getUpStation());
                    this.addVertex(section.getDownStation());
                    DefaultWeightedEdge edge = this.addEdge(section.getUpStation(), section.getDownStation());
                    this.setEdgeWeight(edge, section.getDistance().getValue());
                });

        return new DijkstraShortestPath<>(this);
    }

    public Path findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = Optional.ofNullable(shortestPath.getPath(source, target))
                .orElseThrow(() -> new CannotReachableException(format("%s와(과) %s이(가) 이어져 있지 않습니다."
                        , source.getName()
                        , target.getName())));

        Stations stations = new Stations(graphPath.getVertexList());
        int weight = (int) graphPath.getWeight();

        return new Path(stations, new Distance(weight));
    }
}
