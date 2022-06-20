package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.SubwayFare;

public class LoginMember {
    private static final int CHILD_INTERVAL_START_AGE = 6;
    private static final int CHILD_INTERVAL_END_AGE = 13;
    private static final int TEEN_INTERVAL_START_AGE = 13;
    private static final int TEEN_INTERVAL_END_AGE = 19;

    public static final int DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY = 350;
    public static final int CHILD_DISCOUNT_RATE = 50;
    public static final int TEEN_DISCOUNT_RATE = 20;

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public SubwayFare discountFareByAge(SubwayFare beforeDiscountFare) {
        if (isChild()) {
            return beforeDiscountFare.subtract(DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY)
                    .discountedByPercent(CHILD_DISCOUNT_RATE);
        }
        if (isTeen()) {
            return beforeDiscountFare.subtract(DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY)
                    .discountedByPercent(TEEN_DISCOUNT_RATE);
        }
        return beforeDiscountFare;
    }

    private boolean isChild() {
        return age >= CHILD_INTERVAL_START_AGE && age < CHILD_INTERVAL_END_AGE;
    }

    private boolean isTeen() {
        return age >= TEEN_INTERVAL_START_AGE && age < TEEN_INTERVAL_END_AGE;
    }

    public Long getId() {
        return id;
    }


}
