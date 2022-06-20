package nextstep.subway.path.domain;

public class DistanceFeeHandler extends FeeHandler {
    private static final int FIRST_SECTION_OVER_KR_STANDARD = 10;
    private static final int FIRST_SECTION_DISTANCE_PER_KM_STANDARD = 5;
    private static final int SECOND_SECTION_OVER_KR_STANDARD = 50;
    private static final int SECOND_SECTION_DISTANCE_PER_KM_STANDARD = 8;
    private static final int EXTRA_CHARGE = 100;

    private int distance;

    protected DistanceFeeHandler(FeeHandler feeHandler, int distance) {
        super(feeHandler);
        this.distance = distance;
    }

    @Override
    public void calculate(FeeV2 fee) {
        addDistanceFee(fee);
        super.calculate(fee);
    }

    private void addDistanceFee(FeeV2 fee) {
        fee.add(calculateOverFirstSection());
        fee.add(calculateOverSecondSection());
    }

    private int calculateOverFirstSection() {
        final int minDistance = Math.min(this.distance, SECOND_SECTION_OVER_KR_STANDARD);
        return calculateExtraCharge(minDistance, FIRST_SECTION_OVER_KR_STANDARD,
                FIRST_SECTION_DISTANCE_PER_KM_STANDARD);
    }

    private int calculateOverSecondSection() {
        return calculateExtraCharge(this.distance, SECOND_SECTION_OVER_KR_STANDARD,
                SECOND_SECTION_DISTANCE_PER_KM_STANDARD);
    }

    private int calculateExtraCharge(int distance, int overKilometer, int perDistance) {
        if (distance <= overKilometer) {
            return 0;
        }
        distance -= overKilometer;
        return (int) (Math.ceil((distance - 1) / perDistance) + 1) * EXTRA_CHARGE;
    }
}
