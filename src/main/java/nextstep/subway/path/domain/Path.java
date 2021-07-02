package nextstep.subway.path.domain;

import nextstep.subway.exception.Message;
import nextstep.subway.extracharge.DistanceBasedExtraCharge;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static nextstep.subway.fare.domain.Fare.BASE_FARE;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private Fare fare;

    public Path(GraphPath<Station, SectionEdge> pathResult) {
        if (pathResult == null) {
            throw new IllegalArgumentException(Message.ERROR_PATH_NOT_FOUND.showText());
        }
        stations = pathResult.getVertexList();
        distance = (int) pathResult.getWeight();

        fare = new Fare(BASE_FARE + DistanceBasedExtraCharge.calculate(distance) + getMaxExtraCharge(pathResult));
    }

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
        fare = new Fare(BASE_FARE + DistanceBasedExtraCharge.calculate(distance));
    }

    private int getMaxExtraCharge(GraphPath<Station, SectionEdge> pathResult) {
        Comparator<SectionEdge> comparatorByExtraCharge = Comparator.comparingInt(SectionEdge::getExtraCharge);
        return pathResult.getEdgeList().stream().
                max(comparatorByExtraCharge)
                .orElseThrow(NoSuchElementException::new)
                .extraCharge;
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getFare() {
        return fare.getFare();
    }
}
