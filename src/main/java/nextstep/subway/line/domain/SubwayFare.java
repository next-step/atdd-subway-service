package nextstep.subway.line.domain;

import java.util.Arrays;

/**
 * 지하철 요금 계산
 *
 * @author haedoang
 * @see <a href="http://www.seoulmetro.co.kr/kr/page.do?menuIdx=354">서울교통공사 운임안내</a>
 * @since 1.0
 */
public enum SubwayFare {
    UNTIL_10KM(Distance.MIN_DISTANCE, 10, false, 0, 0, "10km 이내"),
    UNTIL_50KM(11, 50, true, 5, 100, "10km ~ 50km 이내"),
    OVER_50KM(51, Integer.MAX_VALUE, true, 8, 100, "50km 초과");

    public static final int BASE_RATE = 1_250;
    public static final int NO_CHARGE = 0;

    private int minDistance;
    private int maxDistance;
    private boolean useExtraCharge;
    private int per;
    private int charges;
    private String desc;

    public int toChargeDistance(Distance distance) {
        if (isUntil50Km()) {
            return isExceed(distance) ? maxChargeDistance() : chargeDistance(distance);
        }

        if (isOver50Km()) {
            return chargeDistance(distance);
        }
        return NO_CHARGE;
    }

    public boolean includes(Distance distance) {
        return this.minDistance <= distance.intValue() && this.maxDistance >= distance.intValue();
    }

    public int maxChargeDistance() {
        if (isUntil10Km()) {
            return NO_CHARGE;
        }
        return maxDistance - minDistance;
    }

    public int chargeDistance(Distance distance) {
        if (!includes(distance)) {
            return NO_CHARGE;
        }
        return distance.intValue() - minDistance;
    }

    public boolean isExceed(Distance distance) {
        return this.maxDistance < distance.intValue();
    }

    public boolean isUntil10Km() {
        return this == UNTIL_10KM;
    }

    public boolean isUntil50Km() {
        return this == UNTIL_50KM;
    }

    public boolean isOver50Km() {
        return this == OVER_50KM;
    }

    SubwayFare(int minDistance, int maxDistance, boolean useExtraCharge, int per, int charges, String desc) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.useExtraCharge = useExtraCharge;
        this.per = per;
        this.charges = charges;
        this.desc = desc;
    }

    public static int rateInquiry(Distance distance) {
        return Arrays.stream(SubwayFare.values())
                .mapToInt(it -> it.useExtraCharge ? it.calculateOverFare(distance) : BASE_RATE)
                .sum();
    }

    public static SubwayFare of(Distance distance) {
        return Arrays.stream(SubwayFare.values())
                .filter(it -> it.includes(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private int calculateOverFare(Distance distance) {
        int chargeDistance = toChargeDistance(distance);
        return chargeDistance == NO_CHARGE ? NO_CHARGE : (int) ((Math.ceil((chargeDistance) / per) + 1) * charges);
    }
}
