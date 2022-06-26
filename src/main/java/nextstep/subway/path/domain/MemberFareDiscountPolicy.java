package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

/**
 * 청소년 (13 ~ 18): 운임에서 350원을 공제한 금액의 20%할인
 * 어린이 (6 ~ 12) : 운임에서 350원을 공제한 금액의 50%할인
 */
public class MemberFareDiscountPolicy implements FarePolicy {
    private static final int DEDUCTIBLE_AMOUNT = 350;

    private final FarePolicy farePolicy;
    private final int age;

    public MemberFareDiscountPolicy(FarePolicy farePolicy, int age) {
        this.farePolicy = farePolicy;
        this.age = age;
    }

    @Override
    public int fare() {
        MemberGroup memberGroup = MemberGroup.from(age);
        return memberGroup.discount(farePolicy.fare());
    }

    enum MemberGroup {
        BABY(1, 5, fare -> fare),
        CHILDREN(6, 12, MemberGroup::discountChildren),
        TEENAGER(13, 18, MemberGroup::discountTeenager),
        NORMAL(19, 200, fare -> fare);

        private final int minimum;
        private final int maximum;
        private final Function<Integer, Integer> discountable;

        MemberGroup(int minimum, int maximum, Function<Integer, Integer> discountable) {
            this.minimum = minimum;
            this.maximum = maximum;
            this.discountable = discountable;
        }

        public static MemberGroup from(int age) {
            return Arrays.stream(values())
                    .filter(memberGroup -> memberGroup.between(age))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 나이입니다."));
        }

        private boolean between(int age) {
            return minimum <= age && age <= maximum;
        }

        public int discount(int fare) {
            return discountable.apply(fare);
        }

        private static int discountChildren(int fare) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * 0.5);
        }

        private static int discountTeenager(int fare) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * 0.8);
        }
    }
}
