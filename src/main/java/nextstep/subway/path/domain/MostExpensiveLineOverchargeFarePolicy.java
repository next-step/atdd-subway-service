package nextstep.subway.path.domain;

import java.util.Comparator;
import java.util.List;

import nextstep.subway.line.domain.Line;

public class MostExpensiveLineOverchargeFarePolicy implements LineOverchargeFarePolicy {
	@Override
	public int overcharge(int fare, List<Line> lines) {
		Integer maxExtraFare = lines.stream()
			.map(Line::getExtraFare)
			.max(Comparator.comparingInt(o -> o))
			.orElseThrow(IllegalStateException::new);

		return fare + maxExtraFare;
	}
}
