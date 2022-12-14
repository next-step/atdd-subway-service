package nextstep.subway.path.domain;

import static nextstep.subway.fare.domain.Fare.FREE;

import java.util.List;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class Path {

    GraphPath<Station, SectionWeightEdge> path;

    public Path(
        GraphPath<Station, SectionWeightEdge> path) {
        this.path = path;
    }


    public List<Station> shortestPath() {
        return path.getVertexList();
    }

    public Distance getShortestDistance() {
        return new Distance(path.getWeight());
    }

    public Fare getSurcharge() {
        return path.getEdgeList()
            .stream()
            .map(SectionWeightEdge::getSurcharge)
            .max(Fare::compareTo)
            .orElse(FREE);
    }
}
