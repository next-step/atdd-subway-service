package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

/**
 * ### 노선별 추가 요금 정책
 * - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
 * - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
 * - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
 * - 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
 * - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
 */

public class FarePolicyByLine implements FarePolicy {

    private final List<Line> lines;

    public FarePolicyByLine(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public double calculate(double fare) {
        return fare + lines.stream()
                .map(line -> line.getExtraFare())
                .max(Double::compareTo)
                .orElse(0D);
    }
}
