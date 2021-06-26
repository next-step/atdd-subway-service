package nextstep.subway.path.domain;

public class Fare {

    private int distance;

    private Fare(int distance) {
        this.distance = distance;
    }

    public static Fare of(int distance) {
        return new Fare(distance);
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return 0;
    }

}
