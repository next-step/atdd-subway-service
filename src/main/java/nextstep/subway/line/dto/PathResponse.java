package nextstep.subway.line.dto;

import nextstep.subway.line.domain.SectionGraph;
import nextstep.subway.line.domain.Surcharge;
import nextstep.subway.policy.AgeType;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private Fare fare;

    private PathResponse() {
    }

    public PathResponse(SectionGraph graphPath, int surcharge) {
        this(graphPath, surcharge, AgeType.ADULT);
    }

    public PathResponse(SectionGraph graphPath, int surcharge, AgeType ageType) {
        this.stations = graphPath.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        this.distance = graphPath.getTotalDistance();
        Fare fare = Fare.ofByDistance(this.distance, new Surcharge(surcharge));
        this.fare = fare.discountByAge(ageType);
    }

    public List<StationResponse> getStations() {
        return new ArrayList<>(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getFare();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathResponse that = (PathResponse) o;
        return distance == that.distance && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
