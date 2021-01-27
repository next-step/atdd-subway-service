package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Line;

public class LineFarePolicy {
	public static int calculateOverFare(List<Line> lines) {
		return lines.stream()
			.map(Line::getOverFare)
			.reduce(Math::max)
			.orElse(0);
	}
}
