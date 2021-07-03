package nextstep.subway.path.domain.impl;

import nextstep.subway.path.domain.enums.AgeCategory;
import nextstep.subway.path.domain.FarePolicy;

/**
 * ### 로그인 사용자의 경우 연령별 요금 할인 적용
 * - 청소년: 운임에서 350원을 공제한 금액의 20%할인
 * - 청소년: 13세 이상 ~ 19세 미만
 * - 어린이: 운임에서 350원을 공제한 금액의 50%할인
 * - 어린이: 6세 이상 ~ 13세 미만
 */
public class FarePolicyByAge implements FarePolicy {
    private final int age;

    public FarePolicyByAge(int age) {
        this.age = age;
    }

    @Override
    public double calculate(double fare) {
        return AgeCategory.findCategory(age).calculate(fare);
    }
}
