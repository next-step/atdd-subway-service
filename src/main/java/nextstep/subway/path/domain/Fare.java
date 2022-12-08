package nextstep.subway.path.domain;

public class Fare {

    private final int distance;

    protected Fare(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        if (distance <= 10) {
            return 1250;
        }
        return 0;
    }
}
