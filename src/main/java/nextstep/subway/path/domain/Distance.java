package nextstep.subway.path.domain;

public class Distance {

    private int distance;

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance of(int distance) {
        return new Distance(distance);
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return 0;
    }

}
