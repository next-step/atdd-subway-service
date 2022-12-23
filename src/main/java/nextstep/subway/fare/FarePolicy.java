package nextstep.subway.fare;

public class FarePolicy {

    private int lineFare;

    private int distanceFare;

    private int deductionFare;

    private double discountRate;

    public FarePolicy(int lineFare, int distanceFare, int deductionFare, double discountRate) {
        this.lineFare = lineFare;
        this.distanceFare = distanceFare;
        this.deductionFare = deductionFare;
        this.discountRate = discountRate;
    }

    public static FarePolicy of(int lineFare, int distanceFare, int deductionFare, double discountRate) {
        return new FarePolicy(lineFare, distanceFare, deductionFare, discountRate);
    }

    public int calculate() {
        return (int) ((distanceFare + lineFare - deductionFare) * discountRate);
    }
}
