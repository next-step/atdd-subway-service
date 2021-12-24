package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;

import java.util.Arrays;

public enum DiscountRateByAge {
    BABY(6, 1),
    KID(13, 0.5),
    TEENAGER(19, 0.2),
    ADULT(Constants.MAX_AGE, 0);

    int age;
    double discountRate;

    DiscountRateByAge(int age, double discountRate) {
        this.age = age;
        this.discountRate = discountRate;
    }

    public static double getDiscountRate(LoginMember loginMember) {
        int age = Constants.MAX_AGE;

        if (loginMember != null) {
            age = loginMember.getAge();
        }

        int finalAge = age;
        return Arrays.stream(DiscountRateByAge.values())
                .filter(discountRateByAge -> finalAge < discountRateByAge.age)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자 나이가 올바르지 않습니다. 나이 : " + finalAge))
                .discountRate;
    }

    private static class Constants {
        public static final int MAX_AGE = 200;
    }
}
