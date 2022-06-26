package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

/**
 * 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
 *   ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
 *   ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
 * 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
 *   ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
 */
public class LineSurchargePolicy implements FarePolicy {
    public static final int ZERO = 0;

    private final FarePolicy farePolicy;
    private final List<Line> lines;

    public LineSurchargePolicy(FarePolicy farePolicy, List<Line> lines) {
        this.farePolicy = farePolicy;
        this.lines = lines;
    }

    @Override
    public int fare() {
        return getMaxSurcharge(lines) + farePolicy.fare();
    }

    private int getMaxSurcharge(List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(ZERO);
    }
}
