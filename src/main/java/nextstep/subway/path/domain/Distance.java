package nextstep.subway.path.domain;

public class Distance {

    private int distance;

    public Distance() {
    }

    public Distance(int distance) {
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
