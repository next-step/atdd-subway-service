package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFindResult {

    private List<Station> stations;

    private Set<Line> lines;

    private int distance;

    private SubwayFare fare;

    private SubwayFarePolicy policy;

    protected PathFindResult() {

    }

    public PathFindResult(List<Station> stations, Set<Line> lines, int distance) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
        this.fare = SubwayFare.DEFAULT_FARE;
        this.policy = new DefaultSubwayFarePolicy();
    }

    public void applyFarePolicy(SubwayUser subwayUser) {
        int overFareByDistance = policy.calculateOverFareByDistance(distance);
        int overFareByLine = policy.calculateOverFareByLine(lines);
        int middleSum = this.fare.getValue() + overFareByDistance + overFareByLine;
        if (subwayUser.isAnonymous()) {
            this.fare = SubwayFare.of(middleSum);
            return;
        }
        this.fare = policy.discountFareByAge(SubwayFare.of(middleSum), subwayUser);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public SubwayFare getFare() {
        return fare;
    }

    public Set<Line> getLines() {
        return lines;
    }
}
