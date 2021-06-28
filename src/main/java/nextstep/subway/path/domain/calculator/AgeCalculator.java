package nextstep.subway.path.domain.calculator;

import nextstep.subway.auth.domain.DiscountRate;
import nextstep.subway.auth.domain.LoginMember;

public class AgeCalculator {

    public static int discountRate(int fare, LoginMember member) {
        return (int)Math.ceil(fare * getDiscountRate(member));
    }

    public static double getDiscountRate(LoginMember member) {
        Integer age = member.getAge();
        if (age == null || age == 0) {
            return 1;
        }
        return DiscountRate.findDiscountRateByAge(age).getDicountRate();
    }

}
