package nextstep.subway.fare.policy;

import java.math.BigDecimal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.domain.Age;

public class MemberDiscountPolicy extends FarePolicyDecorator {
    public static final Fare DEDUCTIBLE_FARE = Fare.from(350);
    public static final BigDecimal CHILDREN_DISCOUNT_RATE = BigDecimal.valueOf(0.5);
    public static final BigDecimal TEENAGER_DISCOUNT_RATE = BigDecimal.valueOf(0.2);

    private final LoginMember loginMember;

    private MemberDiscountPolicy(FarePolicy farePolicy, LoginMember loginMember) {
        super(farePolicy);
        this.loginMember = loginMember;
    }

    public static FarePolicy of(FarePolicy farePolicy, LoginMember loginMember) {
        return new MemberDiscountPolicy(farePolicy, loginMember);
    }

    @Override
    public Fare calculateFare() {
        if (loginMember.isNonMember()) {
            return super.calculateFare();
        }
        return MemberDiscount.discountFare(super.calculateFare(), Age.from(loginMember.getAge()));
    }
}
