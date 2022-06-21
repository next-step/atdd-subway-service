package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class Path {
    private final GraphPath<Station, SectionEdge> path;

    public Path(GraphPath<Station, SectionEdge> path) {
        this.path = path;
    }

    public List<Station> getStations() {
        return path.getVertexList();
    }

    public Distance getDistance() {
        return new Distance((int) path.getWeight());
    }

    public Integer getMaxLineExtraFare() {
        return path.getEdgeList().stream()
                .map(it -> it.getLine().getExtraFare())
                .max(Integer::compare)
                .orElse(0);
    }
}
