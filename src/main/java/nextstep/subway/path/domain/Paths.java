package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.CustomException;
import nextstep.subway.path.domain.fare.AdditionalFareOfLinePolicy;
import nextstep.subway.path.domain.fare.FareOfAgePolicy;
import nextstep.subway.path.domain.fare.FareOfDistancePolicy;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Objects;

import static nextstep.subway.exception.CustomExceptionMessage.NOT_CONNECTED_SOURCE_AND_TARGET_STATION;
import static nextstep.subway.exception.CustomExceptionMessage.NOT_FOUND_PATHS;

public class Paths {

    private final List<Station> shortestStationRoutes;
    private final int totalDistance;
    private final int maxAdditionalFare;

    private Paths(final GraphPath<Station, SectionEdge> graphPath) {
        checkNotConnectedBetweenStations(graphPath);
        checkNotFoundPaths(graphPath.getWeight());
        this.shortestStationRoutes = graphPath.getVertexList();
        this.totalDistance = (int)graphPath.getWeight();
        this.maxAdditionalFare = getMaxAdditionalFare(graphPath);
    }

    public static Paths of(final GraphPath<Station, SectionEdge> graphPath) {
        return new Paths(graphPath);
    }

    public Fare calculateFare(final LoginMember loginMember) {
        return new Fare().acceptPolicy(new FareOfDistancePolicy(this.totalDistance))
                       .acceptPolicy(new AdditionalFareOfLinePolicy(this.maxAdditionalFare))
                       .acceptPolicy(new FareOfAgePolicy(loginMember.getAge()));
    }

    public List<Station> getShortestStationRoutes() {
        return this.shortestStationRoutes;
    }

    public int getTotalDistance() {
        return this.totalDistance;
    }

    private void checkNotConnectedBetweenStations(final GraphPath<Station, SectionEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new CustomException(NOT_CONNECTED_SOURCE_AND_TARGET_STATION);
        }
    }

    private void checkNotFoundPaths(final double weight) {
        if (Double.isInfinite(weight)) {
            throw new CustomException(NOT_FOUND_PATHS);
        }
    }

    private int getMaxAdditionalFare(GraphPath<Station, SectionEdge> graphPath) {
        return graphPath.getEdgeList()
                        .stream()
                        .mapToInt(SectionEdge::getAdditionalFare)
                        .max().orElse(0);
    }
}
