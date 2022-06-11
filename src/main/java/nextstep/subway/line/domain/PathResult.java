package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class PathResult {
    private List<Station> stations;
    private List<SectionWeightedEdge> sectionEdges;

    public PathResult(GraphPath graphPath) {
        this.stations = graphPath.getVertexList();
        this.sectionEdges = graphPath.getEdgeList();
    }

    public PathResult of(GraphPath graphPath) {
        return new PathResult(graphPath);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getFare() {
        int fare = 1_250;
        int distance = getDistance();

        if (distance > 10 && distance < 50) {
            fare += Math.round((distance - 10) / 5) * 100;
        } else if (distance > 50) {
            fare += Math.round((distance - 10) / 8) * 100;
        }

        return fare;
    }

    public int getDistance() {
        return sectionEdges.stream()
                .mapToInt(SectionWeightedEdge::getDistance)
                .sum();
    }
}
