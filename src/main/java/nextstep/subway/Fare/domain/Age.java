package nextstep.subway.Fare.domain;

import nextstep.subway.auth.domain.LoginMember;

public enum Age {
    CHILD("child", 0.5, 350),
    TEENAGER("teenager", 0.8, 350),
    ADULT("adult", 1.0, 0);

    private String name;
    private Double multipleRate;
    private int discountFare;

    Age(String name, double multipleRate, int discountFare) {
        this.name = name;
        this.multipleRate = multipleRate;
        this.discountFare = discountFare;
    }

    public int getDiscountFare(){
        return this.discountFare;
    }

    public Double getMultipleRate(){
        return this.multipleRate;
    }

    public static Age getPassengerType (LoginMember loginMember) {
        final int CHILD_START_AGE = 6;
        final int CHILD_END_AGE = 13;
        final int TEENAGER_START_AGE = 13;
        final int TEENAGER_END_AGE = 19;

        if (loginMember.isNotLogin()) {
            return ADULT;
        }

        int age = loginMember.getAge();
        validAge(age);
        if (age >= CHILD_START_AGE && age < CHILD_END_AGE) {
            return CHILD;
        }

        if (age >= TEENAGER_START_AGE && age < TEENAGER_END_AGE) {
            return TEENAGER;
        }
        return ADULT;
    }

    private static void validAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("나이는 0보다 작을 수 없습니다.");
        }
    }
}
