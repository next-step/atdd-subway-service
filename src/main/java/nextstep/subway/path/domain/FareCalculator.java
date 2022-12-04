package nextstep.subway.path.domain;

import nextstep.subway.member.domain.MemberDiscountPolicy;

public class FareCalculator {
    public static int calculate(int age, int pathFare, int lineFare) {
        int totalFare = pathFare + lineFare;
        return MemberDiscountPolicy.getFare(age, totalFare);
    }
}
