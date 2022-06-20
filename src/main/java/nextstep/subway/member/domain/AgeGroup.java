package nextstep.subway.member.domain;

import java.util.Arrays;

public enum AgeGroup {
    // 5세 이하의 요구사항은 없어서 우선 비워두었습니다.
    CHILD(6, 13, 350, 0.5),
    TEENAGER(13, 19, 350, 0.8),
    ADULT(19, Integer.MAX_VALUE, 0, 1);

    private final int from;
    private final int to;
    private final int deductibleAmount;
    private final double deductibleRate;

    AgeGroup(int from, int to, int deductibleAmount, double deductibleRate) {
        this.from = from;
        this.to = to;
        this.deductibleAmount = deductibleAmount;
        this.deductibleRate = deductibleRate;
    }

    public static AgeGroup getAgeGroupByAge(int age) {
        return Arrays.stream(AgeGroup.values())
                .filter(group -> group.from <= age && group.to > age)
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
