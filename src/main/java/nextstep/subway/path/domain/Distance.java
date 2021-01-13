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
}
