package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.Age;
import org.apache.commons.lang3.ObjectUtils;

public interface DiscountPolicy {

    int calculateDiscountFare(Integer fare);

    static DiscountPolicy of(Age age) {
        if (ObjectUtils.isEmpty(age)) {
            return new NoneDiscountPolicy();
        }
        if (age.isYouth()) {
            return new YouthDiscountPolicy();
        }
        if (age.isChild()) {
            return new ChildDiscountPolicy();
        }
        return new NoneDiscountPolicy();
    }
}
