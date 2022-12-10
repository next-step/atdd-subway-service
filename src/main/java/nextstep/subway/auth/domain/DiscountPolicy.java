package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public abstract class DiscountPolicy {

    protected MemberMoney defaultCharge = MemberMoney.from(Money.from(1250));

    protected abstract Money getCharge();

    public static DiscountPolicy fromAge(Age age) {
        if (age.isChild()) {
            return new ChildDiscountPolicy();
        }
        if (age.isTeenager()) {
            return new TeenagerDiscountPolicy();
        }
        return new DefaultDiscountPolicy();
    }

    public static DiscountPolicy defaultPolicy() {
        return new DefaultDiscountPolicy();
    }
}
