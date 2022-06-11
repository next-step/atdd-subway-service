package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class PathResult {
    private List<Station> stations;
    private List<SectionWeightedEdge> sectionEdges;
    private int distance;

    public PathResult(GraphPath graphPath) {
        this.stations = graphPath.getVertexList();
        this.sectionEdges = graphPath.getEdgeList();
        this.distance = sectionEdges.stream()
                .mapToInt(SectionWeightedEdge::getDistance)
                .sum();
    }

    public PathResult of(GraphPath graphPath) {
        return new PathResult(graphPath);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getFare() {
        int fare = 1_250 + calculateOverFare() + calulateExtraFare();
        return fare;
    }

    private int calculateOverFare() {
        if (this.distance > 10 && this.distance < 50) {
            return (int) (Math.ceil((this.distance - 10) / 5) * 100);
        } else if (this.distance > 50) {
            return (int) (Math.ceil((this.distance - 50) / 8) * 100);
        }
        return 0;
    }

    private int calulateExtraFare() {
        return sectionEdges.stream()
                .mapToInt(sectionWeightedEdge -> sectionWeightedEdge.getLine().getExtraFare())
                .max().orElseThrow(RuntimeException::new);
    }
}
