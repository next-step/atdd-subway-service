package nextstep.subway.path.domain;

import nextstep.subway.common.Money;

import java.util.List;

public class Path {

    private final PathSections pathSections;

    private final Distance distance;

    public Path(final PathSections pathSections, final int distance) {
        this(pathSections, Distance.valueOf(distance));
    }

    public Path(final PathSections pathSections, final Distance distance) {
        this.pathSections = pathSections;
        this.distance = distance;
    }

    public Money settle(final DistanceFee distanceFee, final LineFee lineFee) {
        Money settledDistanceFee = distanceFee.settle(distance);
        List<Long> lineIds = getLineIds();
        Money settledLineFee = lineFee.settle(lineIds);
        return settledDistanceFee.add(settledLineFee);
    }

    private List<Long> getLineIds() {
        return pathSections.getLineIds();
    }

    public List<PathStation> getPathStations() {
        return pathSections.getPathStations();
    }

    public int getDistance() {
        return distance.getValue();
    }
}
