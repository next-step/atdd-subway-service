package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class Path {

    private static final int DEFAULT_FARE = 0;

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
        Fare resultFare = DistanceFarePolicy.calculate(getDistance());
        resultFare.plus(getExtraFare());
        return resultFare;
    }

    public Fare getExtraFare() {
        return shortestPath.getEdgeList().stream()
                .map(SectionEdge::getLine)
                .map(Line::getExtraFare)
                .max(Fare::compareTo)
                .orElse(new Fare(DEFAULT_FARE));
    }

}
