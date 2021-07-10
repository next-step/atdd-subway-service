package nextstep.subway.path.domain.discountpolicy;

public class DiscountPolicyFactory {

    public static DiscountPolicy getDiscountPolicy(int age) {
        if (age >= 6 && age < 13) {
            return new ChildrenDiscountPolicy();
        }

        if (age >= 13 && age < 19) {
            return new TeenagerDiscountPolicy();
        }

        return (fare) -> fare;
    }

}
