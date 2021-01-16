package nextstep.subway.path.domain;

import static com.google.common.base.Preconditions.checkArgument;

public class Distance {

    private int distance;

    public Distance() {
        this(0);
    }

    public Distance(int distance) {
        checkArgument(distance >= 0, "distance not allowed negative value");

        this.distance = distance;
    }

    public void add(int distance) {
        this.distance += distance;
    }

    public int getValue() {
        return distance;
    }

    public boolean isLessThan(int target) {
        return distance < target;
    }

    public boolean ensureDistanceIsGreaterThanEqual(Distance target, int range) {
        return distance + range <= target.getValue();
    }
}
