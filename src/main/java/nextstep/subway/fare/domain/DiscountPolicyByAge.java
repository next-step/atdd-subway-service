package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;

import java.util.Arrays;
import java.util.Objects;

public enum DiscountPolicyByAge {
    BABY(5, 0, 0),
    KID(12, 0.5, 350),
    TEENAGER(18, 0.8, 350),
    ADULT(Constants.MAX_AGE, 1, 0);

    Integer age;
    double chargeRate;
    SubwayFare discountFare;

    DiscountPolicyByAge(int age, double chargeRate, Integer discountFare) {
        this.age = age;
        this.chargeRate = chargeRate;
        this.discountFare = new SubwayFare(discountFare);
    }

    public static DiscountPolicyByAge of(LoginMember loginMember) {
        Integer age = Constants.MAX_AGE;

        if (!Objects.isNull(loginMember.getAge())) {
            age = loginMember.getAge();
        }

        Integer finalAge = age;

        return Arrays.stream(DiscountPolicyByAge.values())
                .filter(discountRateByAge -> finalAge <= discountRateByAge.age)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자 나이가 올바르지 않습니다. 나이 : " + finalAge));
    }

    private static class Constants {
        public static final int MAX_AGE = 200;
    }
}
