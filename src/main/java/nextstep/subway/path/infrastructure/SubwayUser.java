package nextstep.subway.path.infrastructure;

import java.util.Arrays;

/**
 * 지하철 이용자 정보
 *
 * @author haedoang
 * @since 1.0
 */
public enum SubwayUser {
    INFANT(0, 5, false, false, 0, 0, "유아, 0 ~ 5세, 요금 부가 대상 x, 할인 대상x, 할인율 0%, 공제액 0"),

    CHILD(6, 13, true, true, 50, 350, "어린이, 6 ~ 13세, 요금 부가 대상 o, 할인 대상o, 할인율 50%, 공제액 350"),

    YOUTH(13, 19, true, true, 20, 350, "청소년, 13 ~ 19세, 요금 부가 대상 o, 할인 대상o, 할인율 20%, 공제액 350"),

    ADULT(20, Integer.MAX_VALUE, true, false, 0, 0, "성인, 20 ~ xx세, 요금 부가 대상 o, 할인 대상x, 할인율 0%, 공제액 0");

    private final int minAge;
    private final int maxAge;
    private final boolean payUser;
    private final boolean discountUser;
    private final int discountRate;
    private final int deductibleAmount;
    private final String desc;

    SubwayUser(int minAge, int maxAge, boolean payUser, boolean discountUser, int discountRate, int deductibleAmount, String desc) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.payUser = payUser;
        this.discountUser = discountUser;
        this.discountRate = discountRate;
        this.deductibleAmount = deductibleAmount;
        this.desc = desc;
    }

    public static SubwayUser of(int age) {
        return Arrays.stream(SubwayUser.values())
                .filter(it -> it.includes(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean includes(int age) {
        return minAge <= age && maxAge >= age;
    }

    public boolean isPayUser() {
        return payUser;
    }

    public boolean isDiscountUser() {
        return discountUser;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getDeductibleAmount() {
        return deductibleAmount;
    }
}
