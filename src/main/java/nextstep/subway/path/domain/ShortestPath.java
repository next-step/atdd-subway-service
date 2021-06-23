package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class ShortestPath {

    private final BelongLines belongLines;
    private final List<Station> path;
    private final Distance distance;

    public ShortestPath(BelongLines belongLines, List<Station> path, int distance) {
        this.belongLines = belongLines;
        this.path = path;
        this.distance = new Distance(distance);
    }

    public List<Station> getPath() {
        return path;
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getSubwayFare() {
        return new Fare(distance, belongLines);
    }
}
