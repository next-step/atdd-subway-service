package nextstep.subway.path.domain;

public class FeeCalculator {
    private final int maxExtraFee;
    private final int distance;

    private FeeCalculator(int maxExtraFee, int distance) {
        this.maxExtraFee = maxExtraFee;
        this.distance = distance;

    }

    public static FeeCalculator from(int maxExtraFee, int distance) {
        return new FeeCalculator(maxExtraFee, distance);
    }

    public int getFee(FeeStrategy feeStrategy) {
        return feeStrategy.calculate(distance) + maxExtraFee;
    }
}
