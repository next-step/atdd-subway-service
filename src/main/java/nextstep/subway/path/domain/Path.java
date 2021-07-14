package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.exception.DuplicatePathException;
import nextstep.subway.path.exception.NotConnectedPathException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exeption.NotFoundStationException;

public class Path {
    private WeightedMultigraph<Station, DefaultWeightedEdge> map = new WeightedMultigraph(DefaultWeightedEdge.class);
    private Sections sections = new Sections();

    public Path(List<Line> lines) {
        lines.forEach(line -> {
            line.getStations().forEach(map::addVertex);
            line.getSections().findAll().forEach(section -> {
                sections.add(section);
                DefaultWeightedEdge edge = map.addEdge(section.getUpStation(), section.getDownStation());
                map.setEdgeWeight(edge, section.getDistance());
            });
        });
    }

    public List<Station> findShortestPath(Station start, Station end) {
        return createShortestPath(start, end).getVertexList();
    }

    public int findShortestDistance(Station start, Station end) {
        return (int)createShortestPath(start, end).getWeight();
    }

    private GraphPath<Station, DefaultWeightedEdge> createShortestPath(Station start, Station end) {
        validate(start, end);
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath(map).getPath(start, end);
        if (Objects.isNull(path)) {
            throw new NotConnectedPathException();
        }
        return path;
    }

    private void validate(Station start, Station end) {
        if (start.equals(end)) {
            throw new DuplicatePathException();
        }

        if (!(map.containsVertex(start) && map.containsVertex(end))) {
            throw new NotFoundStationException();
        }
    }

    public Fare findPathFare(Station start, Station end) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = createShortestPath(start, end);

        int distance = (int)shortestPath.getWeight();
        List<Line> lines = new ArrayList<>();
        shortestPath.getEdgeList().forEach(edge -> {
            Station source = map.getEdgeSource(edge);
            Station target = map.getEdgeTarget(edge);
            Section section = sections.findByStation(source, target);
            lines.add(section.getLine());
        });

        Fare basicFare = FareSection.calculateFare(distance);
        Fare surcharge = lines.stream().map(Line::getSurcharge).max(Fare::compare).orElse(new Fare());
        return basicFare.add(surcharge);
    }

    public Fare findPathFare(Station start, Station end, AgePolicy policy) {
        Fare fare = findPathFare(start, end);
        return policy.discount(fare);
    }
}
