package nextstep.subway.path.infra;

import nextstep.subway.path.domain.RatePolicy;

/**
 * @see <a href="http://www.seoulmetro.co.kr/kr/page.do?menuIdx=354">운임안내</a>
 */
public class RatePolicyByDistance implements RatePolicy {
    private static final int DEFAULT_CHARGES = 1250;
    private static final int ADDITIONAL_CHARGES = 100;

    private final double distance;

    public RatePolicyByDistance(final double distance) {
        this.distance = distance;
    }

    @Override
    public double calc(double fee) {
        return calculate() + fee;
    }

    public double calc() {
        return calc(0);
    }

    private double calculate() {
        if (distance <= 10) {
            return DEFAULT_CHARGES;
        }

        if (distance < 50) {
            return calculateByDistance(distance - 10, 5) - ADDITIONAL_CHARGES;
        }

        return calculateByDistance(distance, 8);
    }

    private int calculateByDistance(double distance, int km) {
        return DEFAULT_CHARGES + (int) ((Math.ceil((distance - 1) / km) + 1) * ADDITIONAL_CHARGES);
    }

}
