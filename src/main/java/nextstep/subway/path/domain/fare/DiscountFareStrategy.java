package nextstep.subway.path.domain.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.UserType;

public class DiscountFareStrategy {

    private static final int DISCOUNT_BASE_PRICE = 350;
    private LoginMember loginMember;

    public DiscountFareStrategy(LoginMember loginMember) {
        this.loginMember = loginMember;
    }
    public int getDiscountedFare(int currentCharge){
        if (loginMember.getType().equals(UserType.GUEST)){
            return currentCharge;
        }
        currentCharge-=DISCOUNT_BASE_PRICE;
        currentCharge*=(1-Discount.getDiscountByAge(loginMember.getAge()).getRate());
        return currentCharge;
    }
}
