package nextstep.subway.member.domain;

import java.util.function.Function;

public enum MemberDiscountPolicy {
    CHILD(6, 12, (totalFare) -> (int) Math.ceil((totalFare - 350) * 0.5)),
    TEENAGER(13, 18, (totalFare) -> (int) Math.ceil((totalFare - 350) * 0.8)),
    ADULT(19, Integer.MAX_VALUE, (totalFare) -> totalFare);

    private int min;
    private int max;
    private Function<Integer, Integer> expression;

    MemberDiscountPolicy(int min, int max, Function<Integer, Integer> expression) {
        this.min = min;
        this.max = max;
        this.expression = expression;
    }

    public static int getFare(int age, int totalFare) {
        MemberDiscountPolicy policy = match(age);
        return policy.discount(totalFare);
    }

    private static MemberDiscountPolicy match(int age) {
        if(CHILD.min <= age && age <= CHILD.max) {
            return MemberDiscountPolicy.CHILD;
        }

        if(TEENAGER.min <= age && age <= TEENAGER.max) {
            return MemberDiscountPolicy.TEENAGER;
        }

        return MemberDiscountPolicy.ADULT;
    }

    private int discount(int totalFare) {
        return this.expression.apply(totalFare);
    }
}
