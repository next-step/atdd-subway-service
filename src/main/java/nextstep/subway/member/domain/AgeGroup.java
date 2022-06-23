package nextstep.subway.member.domain;

import java.util.Arrays;

public enum AgeGroup {
    // 5세 이하의 요구사항은 없어서 우선 비워두었습니다.
    CHILD(AgeGroup.MINIMUM_CHILD_AGE, AgeGroup.MINIMUM_TEENAGER_AGE, 350, 0.5),
    TEENAGER(AgeGroup.MINIMUM_TEENAGER_AGE, AgeGroup.MINIMUM_ADULT_AGE, 350, 0.8),
    ADULT(AgeGroup.MINIMUM_ADULT_AGE, Integer.MAX_VALUE, 0, 1);

    public static final int MINIMUM_CHILD_AGE = 6;
    public static final int MINIMUM_TEENAGER_AGE = 13;
    public static final int MINIMUM_ADULT_AGE = 19;

    private final Age from;
    private final Age to;
    private final int deductibleAmount;
    private final double deductibleRate;

    AgeGroup(int from, int to, int deductibleAmount, double deductibleRate) {
        this.from = new Age(from);
        this.to = new Age(to);
        this.deductibleAmount = deductibleAmount;
        this.deductibleRate = deductibleRate;
    }

    public static AgeGroup getAgeGroupByAge(Age age) {
        return Arrays.stream(AgeGroup.values())
                .filter(group -> group.from.isGreaterThenOrEqual(age) && group.to.isLessThen(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("연령 그룹을 찾을 수 없습니다."));
    }

    public int discountLineFare(int lineFare) {
        if (lineFare <= this.deductibleAmount) {
            throw new IllegalArgumentException("운임 비용이 할인 요금보다 작을 수 없습니다.");
        }

        return (int) ((lineFare - this.deductibleAmount) * this.deductibleRate);
    }
}
