package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPath {
    private final GraphPath<Station, SectionEdge> shortestPath;

    public ShortestPath(GraphPath<Station, SectionEdge> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<StationResponse> getStations() {
        return shortestPath.getVertexList()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public int getDistance() {
        return (int) shortestPath.getWeight();
    }

    public Fare getFare(int age) {
        Lines lines = new Lines(shortestPath.getEdgeList()
                .stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toList()));

        return new Fare(lines.maxExtraFare().get(), getDistance(), age);
    }
}
