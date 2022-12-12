package nextstep.subway.path.domain;

public class FeeCalculator {

    private final int maxExtraFee;
    private final int distance;

    private FeeCalculator(int maxExtraFee, int distance) {
        this.maxExtraFee = maxExtraFee;
        this.distance = distance;
    }

    public static FeeCalculator of(Integer maxExtraFee, int distance) {
        return new FeeCalculator(maxExtraFee, distance);
    }

    public int getFee() {
        return calculateOverFare(distance) + maxExtraFee;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
