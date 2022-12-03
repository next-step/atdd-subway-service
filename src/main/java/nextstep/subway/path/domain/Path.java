package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.auth.domain.discount.DiscountPolicy;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private Distance distance;
    private int cost;

    public Path() {
    }

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = new Distance(distance);
        this.cost = this.distance.calculateCost();
    }

    public void addMaxExtraCostInLines() {
        int maxExtraCostInLines = Integer.MIN_VALUE;
        for (Line line : createNoDuplicateLinesInSections()) {
            maxExtraCostInLines = Math.max(maxExtraCostInLines, line.getExtraCost());
        }
        cost = cost + maxExtraCostInLines;
    }

    public Set<Line> createNoDuplicateLinesInSections() {
        Set<Line> lines = new HashSet<>();

        stations.forEach(station -> lines.addAll(station.getLinesInSections()));

        return lines;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    public int getCost() {
        return cost;
    }

    public void applyDiscountPolicy(DiscountPolicy discountPolicy) {
        cost = discountPolicy.discount(cost);
    }
}
