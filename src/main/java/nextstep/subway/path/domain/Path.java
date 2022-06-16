package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> shortestPath;
    private int shortestDistance;
    private int fare;
    private Set<Line> chargedLines;

    public Path(List<Station> shortestPath, int shortestDistance, Set<Line> chargedLines) {
        this.shortestPath = shortestPath;
        this.shortestDistance = shortestDistance;
        this.fare = DistanceFarePolicy.calculateFare(shortestDistance);
        this.chargedLines = chargedLines;
    }

    public List<Station> getShortestPath() {
        return shortestPath;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }

    public int getFare() {
        return fare;
    }

    public Set<Line> getChargedLines() {
        return chargedLines;
    }
}
