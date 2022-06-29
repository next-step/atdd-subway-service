package nextstep.subway.fare.domain.discount;

import nextstep.subway.member.domain.AgeGrade;

public interface DiscountPolicy {

    long calculateDiscountFare(AgeGrade ageGrade, long fare);
}
