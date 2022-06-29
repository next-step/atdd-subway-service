package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class Path {

    private GraphPath<Station, SectionEdge> shortestPath;

    protected Path() {
    }

    public Path(GraphPath<Station, SectionEdge> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<Station> getStations() {
        return shortestPath.getVertexList();
    }

    public int getDistance() {
        return (int) shortestPath.getWeight();
    }

    public Fare getFare() {
        return DistanceFarePolicy.calculate(getDistance());
    }

}
