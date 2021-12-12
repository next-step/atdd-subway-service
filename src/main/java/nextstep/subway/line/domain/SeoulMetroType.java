package nextstep.subway.line.domain;

import java.util.Arrays;

/**
 * 지하철 요금 계산
 *
 * @author haedoang
 * @see <a href="http://www.seoulmetro.co.kr/kr/page.do?menuIdx=354">서울교통공사 운임안내</a>
 * @since 1.0
 */
public enum SeoulMetroType {
    UNTIL_10KM(Distance.MIN_DISTANCE, 10, false, 0, 0, "10km 이내"),
    UNTIL_50KM(11, 50, true, 5, 100, "10km ~ 50km 이내"),
    OVER_50KM(51, Integer.MAX_VALUE, true, 8, 100, "50km 초과");

    public static final int BASE_RATE = 1_250;
    public static final int NO_CHARGE = 0;

    private final int minDistance;
    private final int maxDistance;
    private final boolean useExtraCharge;
    private final int per;
    private final int charges;
    private final String desc;

    SeoulMetroType(int minDistance, int maxDistance, boolean useExtraCharge, int per, int charges, String desc) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.useExtraCharge = useExtraCharge;
        this.per = per;
        this.charges = charges;
        this.desc = desc;
    }

    public static SeoulMetroType of(Distance distance) {
        return Arrays.stream(SeoulMetroType.values())
                .filter(it -> it.includes(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Money rateInquiry(Distance distance) {
        int money = Arrays.stream(SeoulMetroType.values())
                .mapToInt(it -> it.useExtraCharge ? it.calculateOverFare(distance) : BASE_RATE)
                .sum();

        return Money.of(money);
    }

    public static Money rateInquiry(Distance distance, Money money) {
        return money.plus(rateInquiry(distance));
    }

    public static Money rateInquiry(Distance distance, SubwayUser user, Money money) {
        if (!user.isPayUser()) {
            return Money.of(Money.MIN_VALUE);
        }
        Money fare = rateInquiry(distance);
        return money.plus(discountFare(fare, user));
    }

    public static Money discountFare(Money money, SubwayUser user) {
        if (!user.isPayUser()) {
            return Money.of(Money.MIN_VALUE);
        }
        if (!user.isDiscountUser()) {
            return money;
        }
        return money.discount(user.getDeductibleAmount(), user.getDiscountRate());
    }

    private int toChargeDistance(Distance distance) {
        if (isUntil50Km()) {
            return isExceed(distance) ? maxChargeDistance() : chargeDistance(distance);
        }

        if (isOver50Km()) {
            return chargeDistance(distance);
        }
        return NO_CHARGE;
    }

    private boolean includes(Distance distance) {
        return minDistance <= distance.intValue() && maxDistance >= distance.intValue();
    }

    private int maxChargeDistance() {
        if (isUntil10Km()) {
            return NO_CHARGE;
        }
        return maxDistance - minDistance;
    }

    private int chargeDistance(Distance distance) {
        if (!includes(distance)) {
            return NO_CHARGE;
        }
        return distance.intValue() - minDistance;
    }

    private boolean isExceed(Distance distance) {
        return this.maxDistance < distance.intValue();
    }

    private boolean isUntil10Km() {
        return this == UNTIL_10KM;
    }

    private boolean isUntil50Km() {
        return this == UNTIL_50KM;
    }

    private boolean isOver50Km() {
        return this == OVER_50KM;
    }

    private int calculateOverFare(Distance distance) {
        int chargeDistance = toChargeDistance(distance);
        return chargeDistance == NO_CHARGE ? NO_CHARGE : (int) ((Math.ceil((chargeDistance) / per) + 1) * charges);
    }
}
