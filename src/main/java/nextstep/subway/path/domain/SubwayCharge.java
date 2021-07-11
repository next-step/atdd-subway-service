package nextstep.subway.path.domain;

public class SubwayCharge {
    private static final int SUBWAY_DEFAULT_CHARGE = 1250;
    private static final String SUBWAY_CHARGE_ERROR = "지하철 요금은 0보다 작을 수 없습니다.";

    private final int charge;

    public SubwayCharge(int distance, int extraCharge) {
        this(calculateSubwayCharge(distance, extraCharge));
    }

    public SubwayCharge(int charge) {
        validateSubwayCharge(charge);
        this.charge = charge;
    }

    private static int calculateSubwayCharge(int distance, int extraCharge) {
        int charge = SUBWAY_DEFAULT_CHARGE + extraCharge;

        SubwayDistanceChargePolicy subwayDistanceChargePolicy = SubwayDistanceChargePolicy.subwayChargePolicy(distance);
        charge += subwayDistanceChargePolicy.calculateDistanceExtraCharge(distance);

        return charge;
    }

    private void validateSubwayCharge(int subwayCharge) {
        if (subwayCharge < 0) {
            throw new IllegalArgumentException(SUBWAY_CHARGE_ERROR);
        }
    }

    public int charge() {
        return charge;
    }
}
