package nextstep.subway.path.domain;

import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class ShortestPath {

    public static final int BASIC_FARE = 1250;
    public static final int BASIC_DISTANCE = 10;

    private List<Station> stations;
    private int totalDistance;
    private int fare;

    public ShortestPath(GraphPath path) {
        validate(path);

        stations = path.getVertexList();
        totalDistance = (int) path.getWeight();
        fare = BASIC_FARE
                + calculateOverFare(totalDistance - BASIC_DISTANCE)
                + maxAdditionalFareIn(path);
    }

    private void validate(GraphPath path) {
        if (path == null) {
            throw new RuntimeException("최단경로가 Null 입니다.");
        }
    }

    private int maxAdditionalFareIn(GraphPath<Station, SectionEdge> path) {
        List<SectionEdge> sectionEdges = path.getEdgeList();
        sectionEdges.stream()
                .forEach(sectionEdge -> System.out.println(sectionEdge.getAdditionalFare()));

        return sectionEdges.stream()
                .max(Comparator.comparing(sectionEdge -> sectionEdge.getAdditionalFare()))
                .orElseThrow(NoSuchElementException::new)
                .getAdditionalFare();
    }

    private int calculateOverFare(int distance) {
        if (distance > 0) {
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
        return 0;
    }

    public List<Station> stations() {
        return stations;
    }

    public int totalDistance() {
        return totalDistance;
    }

    public int fare() {
        return fare;
    }
}
