package nextstep.subway.common.domain;

import nextstep.subway.path.domain.SurchargeByDistance;

import java.math.BigDecimal;

public class SubwayFare {
    public static final BigDecimal BASIC_FARE = BigDecimal.valueOf(1250);
    private BigDecimal subwayFare = BASIC_FARE;

    public SubwayFare() {
    }

    public SubwayFare(BigDecimal subwayFare) {
        this.subwayFare = subwayFare;
    }

    public static SubwayFare chargeByDistance(int distance) {
        return new SubwayFare(SurchargeByDistance.charge(BASIC_FARE, distance));
    }

    public BigDecimal charged() {
        return subwayFare;
    }
}
