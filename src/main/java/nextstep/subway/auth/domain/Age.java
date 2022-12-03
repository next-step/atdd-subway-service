package nextstep.subway.auth.domain;

import nextstep.subway.auth.domain.discount.ChildrenDiscountPolicy;
import nextstep.subway.auth.domain.discount.DiscountPolicy;
import nextstep.subway.auth.domain.discount.NoDiscountPolicy;
import nextstep.subway.auth.domain.discount.TeenagerDiscountPolicy;

public class Age {

    private Integer age;

    public Age() {
    }

    public Age(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public DiscountPolicy createDiscountPolicy() {
        if (isTeenager()) {
            return new TeenagerDiscountPolicy();
        }

        if (isChildren()) {
            return new ChildrenDiscountPolicy();
        }

        return new NoDiscountPolicy();
    }

    private boolean isChildren() {
        return age >= 6 && age < 13;
    }

    private boolean isTeenager() {
        return age >= 13 && age < 19;
    }
}
