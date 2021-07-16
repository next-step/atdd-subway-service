package nextstep.subway.path.domain;

import nextstep.subway.fare.domain.DiscountByAge;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FaresByDistance;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Age;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.exception.CannotCalculateAdditionalFareException;
import nextstep.subway.path.exception.CannotReachableException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Optional;

import static nextstep.subway.fare.domain.Fare.DEFAULT_FARE;

public class PathGraph extends WeightedMultigraph<Station, DefaultWeightedEdge> {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    private final Sections usedSections;
    private final Age age;

    public PathGraph(Sections sections, Age age) {
        super(DefaultWeightedEdge.class);

        validateConstructor(sections);
        this.usedSections = sections;
        this.age = age;
        this.shortestPath = setUpGraph(sections);
    }

    public Path findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = Optional.ofNullable(shortestPath.getPath(source, target))
                .orElseThrow(() -> new CannotReachableException(source.getName(), target.getName()));

        Stations stations = new Stations(graphPath.getVertexList());
        Distance distance = new Distance((int) graphPath.getWeight());

        Fare fare = calculateFare(stations, distance);

        return new Path(stations, distance, fare);
    }

    private Fare calculateFare(Stations stations, Distance distance) {
        Fare baseFare = DEFAULT_FARE.add(calculateAdditionalFare(stations));
        baseFare = FaresByDistance.calculate(baseFare, distance);
        baseFare = DiscountByAge.calculate(baseFare, age);
        return baseFare;
    }

    private Fare calculateAdditionalFare(Stations stations) {
        return  stations.get().stream()
                .map(usedSections::findMaxFareByStation)
                .max(Fare::compareTo)
                .orElseThrow(() -> new CannotCalculateAdditionalFareException(stations));
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
}
