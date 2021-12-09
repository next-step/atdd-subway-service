package nextstep.subway.line.domain;

import java.util.Arrays;

/**
 * 지하철 이용자 정보
 *
 * @author haedoang
 * @since 1.0
 */
public enum SubwayUser {
    INFANT(0, 5, true, false, 0, 0, "유아, 0 ~ 5세, 요금 부가 대상 x, 할인 대상x, 할인율 0%, 공제액 0"),

    CHILD(6, 13, false, true, 50, 350, "어린이, 6 ~ 13세, 요금 부가 대상 o, 할인 대상o, 할인율 50%, 공제액 350"),

    YOUTH(13, 19, false, true, 20, 350, "청소년, 13 ~ 19세, 요금 부가 대상 o, 할인 대상o, 할인율 20%, 공제액 350"),

    ADULT(20, Integer.MAX_VALUE, false, false, 0, 0, "성인, 20 ~ xx세, 요금 부가 대상 o, 할인 대상x, 할인율 0%, 공제액 0");

    private int minAge;
    private int maxAge;
    private boolean exemptionTarget;
    private boolean discountTarget;
    private int discountRate;
    private int deductibleAmount;
    private String desc;

    SubwayUser(int minAge, int maxAge, boolean exemptionTarget, boolean discountTarget, int discountRate, int deductibleAmount, String desc) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.exemptionTarget = exemptionTarget;
        this.discountTarget = discountTarget;
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

    public boolean isInfant() {
        return this == INFANT;
    }

    public boolean isChild() {
        return this == CHILD;
    }

    public boolean isYouth() {
        return this == YOUTH;
    }

    public boolean isAdult() {
        return this == ADULT;
    }

}
