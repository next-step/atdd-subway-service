package nextstep.subway.path.domain;

public class SubwayCharge {
    private static final int SUBWAY_DEFAULT_CHARGE = 1250;
    private static final String SUBWAY_CHARGE_ERROR = "지하철 요금은 0보다 작을 수 없습니다.";

    private final int charge;

    public SubwayCharge(SubwayPath subwayPath, Integer age) {
        this(calculateSubwayCharge(subwayPath, age));
    }

    public SubwayCharge(int charge) {
        validateSubwayCharge(charge);
        this.charge = charge;
    }

    private static int calculateSubwayCharge(SubwayPath subwayPath, Integer age) {
        int distance = subwayPath.distance();
        int charge = SUBWAY_DEFAULT_CHARGE + subwayPath.extraCharge();

        SubwayDistanceChargePolicy subwayDistanceChargePolicy = SubwayDistanceChargePolicy.subwayChargePolicy(distance);
        charge += subwayDistanceChargePolicy.calculateDistanceExtraCharge(distance);
        charge = chargeDisCount(charge, age);
        return charge;
    }

    private void validateSubwayCharge(int subwayCharge) {
        if (subwayCharge < 0) {
            throw new IllegalArgumentException(SUBWAY_CHARGE_ERROR);
        }
    }

    private static int chargeDisCount(int charge, Integer age) {
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.ageDiscountPolicy(age);
        return ageDiscountPolicy.calculateAgeDiscountCharge(charge);
    }

    public int charge() {
        return charge;
    }
}
