package nextstep.subway.fare.domain;

public class Fare {

    private static final long DEFAULT_fARE = 1250;
    private final int distance;
    private final int extraFare;

    public Fare(final int distance, final int extraFare) {
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public long calculate() {
        if (distance == 0) {
            return 0;
        }

        return DEFAULT_fARE + calculateFirstInterval() + calculateSecondInterval() + extraFare;
    }

    private int calculateFirstInterval() {
        return Math.max(Math.min(distance / 5 - 1, 8) * 100, 0);
    }

    private int calculateSecondInterval() {
        if (distance <= 50) {
            return 0;
        }

        return (int)Math.ceil((distance - 50) / 8.0) * 100;
    }
}
