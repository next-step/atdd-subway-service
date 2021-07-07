package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Line;

/**
 * 노선에 추가 요금 필드를 추가
 * 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
 * 	- ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
 * 	- ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
 * 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
 * 	- ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
 */
public class LineFarePolicy {

	private final List<Line> lines;

	public LineFarePolicy(List<Line> lines) {
		this.lines = lines;
	}

	public int fare() {
		return lines.stream()
			.map(Line::getExtraFare)
			.max((x, y) -> Integer.compare(x, y))
			.orElse(0);
	}
}
