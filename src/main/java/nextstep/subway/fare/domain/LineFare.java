package nextstep.subway.fare.domain;

import java.util.List;

import nextstep.subway.fare.exception.FareNotFoundException;
import nextstep.subway.line.domain.Line;

public class LineFare {

	public static Fare calculate(List<Line> lines) {
		return Fare.of(lines.stream()
			.mapToInt(Line::getFare)
			.max()
			.orElseThrow(FareNotFoundException::new));
	}
}
