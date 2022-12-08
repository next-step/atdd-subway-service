package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public abstract class DiscountPolicy {

    protected Money defaultCharge = new Money(1250);

    protected abstract Money getCharge();

    public static DiscountPolicy from(Age age) {
        if (age.isChild()) {
            return new ChildDiscountPolicy();
        }
        if (age.isTeenager()) {
            return new TeenagerDiscountPolicy();
        }
        return new DefaultDiscountPolicy();
    }
}
